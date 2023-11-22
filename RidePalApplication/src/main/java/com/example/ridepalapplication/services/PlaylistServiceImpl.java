package com.example.ridepalapplication.services;

import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.exceptions.EntityDuplicateException;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.helpers.CheckPermissions;
import com.example.ridepalapplication.models.*;
import com.example.ridepalapplication.repositories.GenreRepository;
import com.example.ridepalapplication.repositories.PlaylistRepository;
import com.example.ridepalapplication.repositories.SongRepository;
import com.example.ridepalapplication.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.ridepalapplication.helpers.CheckPermissions.checkAuthorization;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    private final GenreRepository genreRepository;
    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;
    private final TagRepository tagRepository;

    @Autowired
    public PlaylistServiceImpl(GenreRepository genreRepository, SongRepository songRepository, PlaylistRepository playlistRepository, TagRepository tagRepository) {
        this.genreRepository = genreRepository;
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
        this.tagRepository = tagRepository;
    }


    @Override
    public List<Playlist> getAll(String name, Integer minDuration, Integer maxDuration, String tag) {
        if (name == null) {
            name = "";
        }
        if (minDuration == null) {
            minDuration = 0;
        }
        if (maxDuration == null) {
            maxDuration = Integer.MAX_VALUE;
        }

//        Tag tagToCheck = tagRepository.findByName(tag);
//        Set<Tag> tagSet = new HashSet<>();
//        tagSet.add(tagToCheck);
//        Collection collection = new HashSet();
//        collection.add(tagSet);

        return playlistRepository.findPlaylistByNameContainingAndDurationBetweenOrderByRankDesc(name, minDuration, maxDuration);
    }

    @Override
    public Optional<Playlist> getById(long id) {
        Optional<Playlist> playlist = playlistRepository.findById(id);
        if (playlist.isEmpty()) {
            throw new EntityNotFoundException("Playlist", id);
        } else return playlist;
    }

    @Override
    public Playlist generatePlaylist(Playlist playlist, int travelDuration, List<GenreDto> genreDtoList) {
        Set<Song> playlistSongs = new HashSet<>();
        int totalPlaylistDuration = 0;
        Long totalRank = 0L;
        for (GenreDto genreDto : genreDtoList) {
            int getGenrePercentage = genreDto.getPercentage();
            int currentGenreDuration = 0;
            int totalGenreDuration = (travelDuration * getGenrePercentage) / 100;

            Genre genre = genreRepository.findByName(genreDto.getName());

            if (genre == null) {
                throw new EntityNotFoundException("Genre", "name", genreDto.getName());
            }

            List<Long> artistsId = new ArrayList<>();

            while (currentGenreDuration < totalGenreDuration) {
                List<Song> songs;

                if (artistsId.isEmpty()) {
                    songs = songRepository.getMeSingleSongByGenre(genre.getId());

                } else {
                    songs = songRepository.getMeSingleSongByGenreAndUniqueArtist(genre.getId(), artistsId);

                    if (songs.isEmpty()) {
                        throw new UnsupportedOperationException(String.format("The application does not support enough unique songs with genre " +
                                "%s to satisfy your request !", genre.getName()));
                    }
                }

                Long artistId = songs.get(0).getArtist().getId();
                artistsId.add(artistId);

                playlistSongs.add(songs.get(0));
                totalRank += songs.get(0).getRank();
                currentGenreDuration += songs.get(0).getDuration();
                totalPlaylistDuration += songs.get(0).getDuration();
            }

        }
        playlist.setRank(totalRank / playlistSongs.size());
        playlist.setDuration(totalPlaylistDuration);
        playlist.setSongs(playlistSongs);
        return playlistRepository.save(playlist);
    }

    public Playlist updateName(User user, Playlist playlistToUpdate, String newName) {
        checkAuthorization(user, playlistToUpdate.getCreator(), "update playlist name");
        playlistToUpdate.setName(newName);
        return playlistRepository.save(playlistToUpdate);
    }

    @Override
    public Playlist updateSong(User user, Song songToUpdate, Playlist playlistToUpdate, boolean isAdded) {
        checkAuthorization(user, playlistToUpdate.getCreator(), "update playlist songs");
        int currentPlaylistDuration = playlistToUpdate.getDuration();

        Long currentSongDuration = songToUpdate.getDuration();
        int temp = Math.toIntExact(currentSongDuration);

        if (isAdded) {
            playlistToUpdate.addSong(songToUpdate);
            currentPlaylistDuration += temp;
        } else {
            playlistToUpdate.removeSong(songToUpdate);
            currentPlaylistDuration -= temp;
        }

        playlistToUpdate.setDuration(currentPlaylistDuration);
        playlistRepository.save(playlistToUpdate);
        return playlistToUpdate;
    }

    @Override
    public void createTag(User user, Tag tag, Playlist playlistToUpdate) {
        checkAuthorization(user, playlistToUpdate.getCreator(), "add tags to other users' playlists");

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
        checkAuthorization(user, playlistToUpdate.getCreator(), "delete tags from other users' playlists");

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

        CheckPermissions.checkAuthorization(user, playlistToDelete.getCreator(), "delete the playlist");

        playlistRepository.deleteById(id);
    }
}
