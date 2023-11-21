package com.example.ridepalapplication.services;

import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.models.Song;
import com.example.ridepalapplication.repositories.SongRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    public SongServiceImpl(SongRepository songRepository) {
        this.songRepository = songRepository;
    }


    @Override
    public Song getByTitleAndArtist(String name, String artist) {
        List<Song> song = songRepository.findByTitleAndArtistName(name, artist);
        if (!song.isEmpty()) {
            return song.get(0);
        }
        throw new EntityNotFoundException("Song", "name", name);
    }
}
