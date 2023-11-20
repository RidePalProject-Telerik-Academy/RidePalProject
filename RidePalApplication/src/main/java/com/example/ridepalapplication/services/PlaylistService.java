package com.example.ridepalapplication.services;

import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.User;

import java.util.List;

public interface PlaylistService {

    Playlist generatePlaylist(Playlist playlist,int travelDuration, List<GenreDto> genreDtoList);
}
