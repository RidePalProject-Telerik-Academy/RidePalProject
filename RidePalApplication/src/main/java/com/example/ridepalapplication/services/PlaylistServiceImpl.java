package com.example.ridepalapplication.services;

import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.models.Genre;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.Song;
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


}
