package com.example.ridepalapplication.services;

import com.example.ridepalapplication.dtos.GenreDto;
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
    public Playlist generatePlaylist(String name,int travelDuration, List<GenreDto> genreDtoList, User user) {
        Set<Song> songs = new HashSet<>();
        for (int i = 0; i <genreDtoList.size() ; i++) {
            int genrePercantage = genreDtoList.get(i).getPercentage();
            int queryCalculation = (travelDuration * genrePercantage) / 100;
            int temp = 0;
            while (temp < queryCalculation) {
                Genre genre = genreRepository.findByName(genreDtoList.get(i).getName());

                List<Song> song = songRepository.getMeSingleSongByGenre(genre.getId());
                if (song.isEmpty()) {
                    break;
                }
                songs.add(song.get(0));
                temp+=song.get(0).getDuration();
            }

        }
        Playlist p = new Playlist();
        p.setCreator(user);
        p.setName(name);
        p.setSongs(songs);
       return playlistRepository.save(p);
    }
}
