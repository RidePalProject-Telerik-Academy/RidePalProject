package com.example.ridepalapplication.services;

import com.example.ridepalapplication.models.Artist;
import com.example.ridepalapplication.models.Song;
import com.example.ridepalapplication.models.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface SongService {

    Song getByTitleAndArtist(String name, String artist);
    Optional<Song> getById(Long id);
    List<Song> findAll(Integer page,Integer pageSize,String songTitle,String artist);
}
