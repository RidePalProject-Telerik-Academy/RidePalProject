package com.example.ridepalapplication.services;

import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.Song;
import com.example.ridepalapplication.models.User;

import java.util.List;
import java.util.Optional;

public interface PlaylistService {

    List<Playlist> getAll();

    Optional<Playlist> getById(long id);

    Playlist generatePlaylist(Playlist playlist, int travelDuration, List<GenreDto> genreDtoList);

    Playlist updateName(User user, Playlist playlistToUpdate, String newName);

    Playlist updateSong(User user, Song songToUpdate, Playlist playlistToUpdate, boolean isAdded);

    void delete(User user, long id);

}
