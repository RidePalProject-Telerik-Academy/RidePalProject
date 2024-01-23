package com.example.ridepalapplication.services;

import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.dtos.PlaylistDto;
import com.example.ridepalapplication.exceptions.EntityDuplicateException;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.helpers.AuthorizationHelper;
import com.example.ridepalapplication.helpers.PlaylistHelper;
import com.example.ridepalapplication.models.*;
import com.example.ridepalapplication.repositories.GenreRepository;
import com.example.ridepalapplication.repositories.PlaylistRepository;
import com.example.ridepalapplication.repositories.SongRepository;
import com.example.ridepalapplication.repositories.TagRepository;
import com.example.ridepalapplication.services.contracts.PlaylistService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class PlaylistServiceImpl implements PlaylistService {
    private final GenreRepository genreRepository;
    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;
    private final TagRepository tagRepository;
    private final AuthorizationHelper authorizationHelper;

    @Autowired
    public PlaylistServiceImpl(GenreRepository genreRepository, SongRepository songRepository, PlaylistRepository playlistRepository, TagRepository tagRepository, AuthorizationHelper authorizationHelper) {
        this.genreRepository = genreRepository;
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
        this.tagRepository = tagRepository;
        this.authorizationHelper = authorizationHelper;
    }
    @Override
    public List<Playlist> getAll() {
        return playlistRepository.findAll();
    }

    @Override
    public List<Playlist> getAll(Integer page, Integer pageSize, String name, Integer minDuration, Integer maxDuration, List<String> genres) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Specification<Playlist> playlistSpecification;

        Specification<Playlist> titleSpec = (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.upper(root.get("name").as(String.class)), "%" + name + "%");

        Specification<Playlist> durationSpec = (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("duration"), minDuration, maxDuration);
        Specification<Playlist> tagNameSpec;
        if (!genres.isEmpty()) {
            tagNameSpec = (root, query, criteriaBuilder) -> {

                Join<Playlist, Genre> tagJoin = root.join("genres", JoinType.INNER);
                return tagJoin.get("name").as(String.class).in(genres);


            };
            playlistSpecification = Specification.allOf(titleSpec, durationSpec, tagNameSpec);
        } else playlistSpecification = Specification.allOf(titleSpec, durationSpec);

        return playlistRepository.findAll(playlistSpecification, pageRequest
                        .withSort(Sort.Direction.DESC, "rank"))
                .getContent();
    }

    @Override
    public List<Playlist> getUserPlaylists(Long id) {
        return playlistRepository.findPlaylistByCreatorId(id);
    }

    @Override
    public Optional<Playlist> getById(long id) {
        Optional<Playlist> playlist = playlistRepository.findById(id);
        if (playlist.isEmpty()) {
            throw new EntityNotFoundException("Playlist", id);
        } else return playlist;
    }

    @Override
    public Playlist choosePlaylistStrategy(PlaylistDto playlistDto, Playlist playlist, int travelDuration) {
       if(playlistDto.uniqueArtists() && !playlistDto.topRank()){
           return generateDefaultRankUniqueArtistsPlaylist(playlist,travelDuration,playlistDto.getGenreDtoList());
       }
       if(playlistDto.topRank() && !playlistDto.uniqueArtists()){
           return generateTopRankSongsNonUniqueArtistPlaylist(playlist,travelDuration,playlistDto.getGenreDtoList());
       }
       if(playlistDto.isTopRank() && playlistDto.isUniqueArtists()){
           return generateTopRankSongsUniqueArtistsPlaylist(playlist,travelDuration,playlistDto.getGenreDtoList());
       }
       else return generateDefaultRankNonUniqueArtistPlaylist(playlist,travelDuration,playlistDto.getGenreDtoList());

    }

    @Override
    public Playlist generateDefaultRankUniqueArtistsPlaylist(Playlist playlist, int travelDuration, List<GenreDto> genreDtoList) {
        Set<Song> playlistSongs = new HashSet<>();
        Set<Genre> genreList = new HashSet<>();
        List<Long> artistsId = new ArrayList<>();
        int totalPlaylistDuration = 0;
        Long totalRank = 0L;
        for (GenreDto genreDto : genreDtoList) {
            if (PlaylistHelper.isPercentageZero(genreDto)) continue;
            int getGenrePercentage = genreDto.getPercentage();
            int currentGenreDuration = 0;
            int totalGenreDuration = (travelDuration * getGenrePercentage) / 100;

            Genre genre = PlaylistHelper.extractGenres(genreRepository, genreDto);
            genreList.add(genre);

            while (currentGenreDuration < totalGenreDuration) {
                List<Song> songs;

                if (artistsId.isEmpty()) {
                    songs = songRepository.getMeSingleSongByGenre(genre.getId());

                } else {
                    songs = songRepository.getMeSingleSongByGenreAndUniqueArtist(genre.getId(), artistsId);

                    PlaylistHelper.validateGenreAvailability(songs, genre);
                }
                PlaylistHelper.getSongArtist(songs, artistsId);

                playlistSongs.add(songs.get(0));
                totalRank += songs.get(0).getRank();
                currentGenreDuration += songs.get(0).getDuration();
                totalPlaylistDuration += songs.get(0).getDuration();
            }

        }
        PlaylistHelper.updatePlaylistDetails(playlist, totalRank, playlistSongs, totalPlaylistDuration, genreList);
        return playlistRepository.save(playlist);
    }

    @Override
    public Playlist generateTopRankSongsNonUniqueArtistPlaylist(Playlist playlist, int travelDuration, List<GenreDto> genreDtoList) {
        Set<Song> playlistSongs = new HashSet<>();
        Set<Genre> genreList = new HashSet<>();
        int totalPlaylistDuration = 0;
        Long totalRank = 0L;
        for (GenreDto genreDto : genreDtoList) {
            if (PlaylistHelper.isPercentageZero(genreDto)) continue;
            int getGenrePercentage = genreDto.getPercentage();
            int currentGenreDuration = 0;
            int totalGenreDuration = (travelDuration * getGenrePercentage) / 100;

            Genre genre = PlaylistHelper.extractGenres(genreRepository, genreDto);
            genreList.add(genre);

            while (currentGenreDuration < totalGenreDuration) {
                List<Song> songs = songRepository.getMeSingleTopSongByGenre(genre.getId());

                PlaylistHelper.validateGenreAvailability(songs, genre);

                playlistSongs.add(songs.get(0));
                totalRank += songs.get(0).getRank();
                currentGenreDuration += songs.get(0).getDuration();
                totalPlaylistDuration += songs.get(0).getDuration();

            }
        }
        PlaylistHelper.updatePlaylistDetails(playlist, totalRank, playlistSongs, totalPlaylistDuration, genreList);
        return playlistRepository.save(playlist);
    }


    @Override
    public Playlist generateTopRankSongsUniqueArtistsPlaylist(Playlist playlist, int travelDuration, List<GenreDto> genreDtoList) {
        Set<Song> playlistSongs = new HashSet<>();
        Set<Genre> genreList = new HashSet<>();
        List<Long> artistsId = new ArrayList<>();
        int totalPlaylistDuration = 0;
        Long totalRank = 0L;
        for (GenreDto genreDto : genreDtoList) {
            if (PlaylistHelper.isPercentageZero(genreDto)) continue;
            int getGenrePercentage = genreDto.getPercentage();
            int currentGenreDuration = 0;
            int totalGenreDuration = (travelDuration * getGenrePercentage) / 100;

            Genre genre = PlaylistHelper.extractGenres(genreRepository, genreDto);
            genreList.add(genre);

            while (currentGenreDuration < totalGenreDuration) {
                List<Song> songs;

                if (artistsId.isEmpty()) {
                    songs = songRepository.getMeSingleTopSongByGenre(genre.getId());

                } else {
                    songs = songRepository.getMeSingleTopSongByGenreAndUniqueArtist(genre.getId(), artistsId);

                    PlaylistHelper.validateGenreAvailability(songs, genre);
                }
                PlaylistHelper.getSongArtist(songs, artistsId);

                playlistSongs.add(songs.get(0));
                totalRank += songs.get(0).getRank();
                currentGenreDuration += songs.get(0).getDuration();
                totalPlaylistDuration += songs.get(0).getDuration();
            }

        }
        PlaylistHelper.updatePlaylistDetails(playlist, totalRank, playlistSongs, totalPlaylistDuration, genreList);
        return playlistRepository.save(playlist);

    }


    @Override
    public Playlist generateDefaultRankNonUniqueArtistPlaylist(Playlist playlist, int travelDuration, List<GenreDto> genreDtoList) {
        Set<Song> playlistSongs = new HashSet<>();
        Set<Genre> genreList = new HashSet<>();
        int totalPlaylistDuration = 0;
        Long totalRank = 0L;
        for (GenreDto genreDto : genreDtoList) {
            if (PlaylistHelper.isPercentageZero(genreDto)) continue;
            int getGenrePercentage = genreDto.getPercentage();
            int currentGenreDuration = 0;
            int totalGenreDuration = (travelDuration * getGenrePercentage) / 100;

            Genre genre = PlaylistHelper.extractGenres(genreRepository, genreDto);

            genreList.add(genre);

            while (currentGenreDuration < totalGenreDuration) {
                List<Song> songs = songRepository.getMeSingleSongByGenre(genre.getId());

                PlaylistHelper.validateGenreAvailability(songs, genre);

                playlistSongs.add(songs.get(0));
                totalRank += songs.get(0).getRank();
                currentGenreDuration += songs.get(0).getDuration();
                totalPlaylistDuration += songs.get(0).getDuration();

            }
        }
        PlaylistHelper.updatePlaylistDetails(playlist, totalRank, playlistSongs, totalPlaylistDuration, genreList);
        return playlistRepository.save(playlist);
    }



    @Override
    public Playlist update(User user, Playlist playlistToUpdate, String newName) {
        authorizationHelper.checkAuthorization(user, playlistToUpdate.getCreator(), "update playlist name");
        playlistToUpdate.setName(newName);
        return playlistRepository.save(playlistToUpdate);
    }

    @Override
    public Playlist addSong(User user, Song songToAdd, Playlist playlistToUpdate) {
        authorizationHelper.checkAuthorization(user, playlistToUpdate.getCreator(), "update playlist songs");

        if (playlistToUpdate.getSongs().contains(songToAdd)) {
            throw new EntityDuplicateException("Song", "title", songToAdd.getTitle(), "in the playlist");
        }

        PlaylistHelper.updatePlaylistDetails(playlistToUpdate, songToAdd, "add");
        return playlistRepository.save(playlistToUpdate);
    }

    @Override
    public Playlist deleteSong(User user, Song songToDelete, Playlist playlistToUpdate) {
        authorizationHelper.checkAuthorization(user, playlistToUpdate.getCreator(), "update playlist songs");

        if (!playlistToUpdate.getSongs().contains(songToDelete)) {
            throw new EntityNotFoundException("Song", "title", songToDelete.getTitle());
        }

        PlaylistHelper.updatePlaylistDetails(playlistToUpdate, songToDelete, "delete");
        playlistRepository.save(playlistToUpdate);
        return playlistToUpdate;
    }

    @Override
    public void createTag(User user, Tag tag, Playlist playlistToUpdate) {
        authorizationHelper.checkAuthorization(user, playlistToUpdate.getCreator(), "add tags to other users' playlists");

        if (tagRepository.findByName(tag.getName()) == null) {
            tagRepository.saveAndFlush(tag);
        }

        Tag tagToAdd = tagRepository.findByName(tag.getName());

        if (playlistToUpdate.getTags().contains(tagToAdd)) {
            throw new EntityDuplicateException("Tag", "name", tag.getName());
        }
        playlistToUpdate.setTags(tagToAdd);
        playlistRepository.save(playlistToUpdate);
    }

    @Override
    public void deleteTag(User user, Tag tag, Playlist playlistToUpdate) {
        authorizationHelper.checkAuthorization(user, playlistToUpdate.getCreator(), "delete tags from other users' playlists");

        Tag tagToDelete = tagRepository.findByName(tag.getName());

        if (!playlistToUpdate.getTags().contains(tagToDelete)) {
            throw new EntityNotFoundException("Tag", "name", tag.getName());
        }

        playlistToUpdate.getTags().remove(tagToDelete);
        playlistRepository.save(playlistToUpdate);
    }

    @Override
    public void delete(User user, long id) {

        Playlist playlistToDelete = playlistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Playlist", id));

        authorizationHelper.checkAuthorization(user, playlistToDelete.getCreator().getId(), "delete the playlist");

        playlistRepository.deleteById(id);
    }

    @Override
    public List<Playlist> getMostRecent() {
        return playlistRepository.getMostRecent();
    }


}
