package com.example.ridepalapplication.services;

import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.Song;
import com.example.ridepalapplication.models.Tag;
import com.example.ridepalapplication.models.User;

import java.util.List;
import java.util.Optional;

public interface PlaylistService {

    List<Playlist> getAll(String name, Integer minDuration, Integer maxDuration, List<String> tags);

    Optional<Playlist> getById(long id);

    Playlist generatePlaylist(Playlist playlist, int travelDuration, List<GenreDto> genreDtoList);

    Playlist updateName(User user, Playlist playlistToUpdate, String newName);

    Playlist addSong(User user, Song songToAdd, Playlist playlistToUpdate);

    Playlist deleteSong(User user, Song songToDelete, Playlist playlistToUpdate);

    void createTag(User user, Tag tag, Playlist playlistToUpdate);

    void deleteTag(User user, Tag tag, Playlist playlistToUpdate);

    void delete(User user, long id);

}
