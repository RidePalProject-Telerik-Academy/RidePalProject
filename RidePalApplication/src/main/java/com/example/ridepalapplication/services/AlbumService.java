package com.example.ridepalapplication.services;

import com.example.ridepalapplication.models.Album;
import com.example.ridepalapplication.models.Song;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface AlbumService {

   Optional <Album> getById(Long id);

    Page <Album> findAll(Integer page, Integer pageSize, String albumTitle, String genre);
}
