package com.example.ridepalapplication.services;

import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.exceptions.AuthorizationException;
import com.example.ridepalapplication.models.Genre;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.Song;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.repositories.GenreRepository;
import com.example.ridepalapplication.repositories.PlaylistRepository;
import com.example.ridepalapplication.repositories.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public Playlist generatePlaylist(Playlist playlist,int travelDuration, List<GenreDto> genreDtoList) {
        Set<Song> songs = new HashSet<>();

        for (GenreDto genreDto : genreDtoList) {
            int getGenrePercentage = genreDto.getPercentage();
            int totalGenreDuration = (travelDuration * getGenrePercentage) / 100;
            int currentGenreDuration = 0;
            Genre genre = genreRepository.findByName(genreDto.getName());
            while (currentGenreDuration < totalGenreDuration) {
                List<Song> song = songRepository.getMeSingleSongByGenre(genre.getId());
                if (song.isEmpty()) {
                    //TODO implement exception
                    break;
                }
                songs.add(song.get(0));
                currentGenreDuration += song.get(0).getDuration();
            }

        }
        playlist.setSongs(songs);
       return playlistRepository.save(playlist);
    }


}
