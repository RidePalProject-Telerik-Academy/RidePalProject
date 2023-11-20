package com.example.ridepalapplication.services;

import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    @Override
    public Playlist generatePlaylist(int travelDuration, List<GenreDto> genreDtoList, User user) {
        return null;
    }
}
