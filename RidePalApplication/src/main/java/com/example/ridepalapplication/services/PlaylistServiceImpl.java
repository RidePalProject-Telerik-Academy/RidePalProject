package com.example.ridepalapplication.services;

import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.helpers.CheckPermissions;
import com.example.ridepalapplication.models.Genre;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.Song;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.repositories.GenreRepository;
import com.example.ridepalapplication.repositories.PlaylistRepository;
import com.example.ridepalapplication.repositories.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.ridepalapplication.helpers.CheckPermissions.checkAuthorization;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    private final GenreRepository genreRepository;
    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;

    @Autowired
    public PlaylistServiceImpl(GenreRepository genreRepository, SongRepository songRepository, PlaylistRepository playlistRepository) {
        this.genreRepository = genreRepository;
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
    }


    @Override
    public List<Playlist> getAll() {
        return playlistRepository.findAll();
    }

    @Override
    public Optional<Playlist> getById(long id) {
        Optional<Playlist> playlist = playlistRepository.findById(id);
        if (playlist.isEmpty()) {
            throw new EntityNotFoundException("Playlist", id);
        } else return playlist;
    }

    @Override
    public Playlist generatePlaylist(Playlist playlist,int travelDuration,List<GenreDto> genreDtoList) {
        Set<Song> playlistSongs = new HashSet<>();
        int totalPlaylistDuration = 0;
        for (GenreDto genreDto : genreDtoList) {
            int getGenrePercentage = genreDto.getPercentage();
            int currentGenreDuration = 0;
            int totalGenreDuration = (travelDuration * getGenrePercentage) / 100;

            Genre genre = genreRepository.findByName(genreDto.getName());

            if(genre == null){
                throw new EntityNotFoundException("Genre","name",genreDto.getName());
            }

            while (currentGenreDuration < totalGenreDuration) {
                List<Song> songs = songRepository.getMeSingleSongByGenre(genre.getId());

                boolean artistExist =  playlistSongs.stream().anyMatch(song -> song.getArtist().equals(songs.get(0).getArtist()));
                    if(artistExist){
                 continue;
                     }

                if (songs.isEmpty()) {
                    //TODO implement exception
                    break;
                }
                playlistSongs.add(songs.get(0));
                currentGenreDuration  += songs.get(0).getDuration();
                totalPlaylistDuration += songs.get(0).getDuration();
            }

        }
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
        if (isAdded) {
            playlistToUpdate.addSong(songToUpdate);
        } else {
            playlistToUpdate.removeSong(songToUpdate);
        }
        playlistRepository.save(playlistToUpdate);
        return playlistToUpdate;
    }

    @Override
    public void delete(User user, long id) {

        Playlist playlistToDelete = playlistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Playlist", id));

        CheckPermissions.checkAuthorization(user, playlistToDelete.getCreator(), "delete the playlist");

        playlistRepository.deleteById(id);
    }


}
