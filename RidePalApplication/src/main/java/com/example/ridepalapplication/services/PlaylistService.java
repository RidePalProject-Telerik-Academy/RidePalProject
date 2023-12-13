package com.example.ridepalapplication.services;

import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.dtos.PlaylistDto;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.Song;
import com.example.ridepalapplication.models.Tag;
import com.example.ridepalapplication.models.User;

import java.util.List;
import java.util.Optional;

public interface PlaylistService {

    List<Playlist> getAll(Integer page,Integer pageSize,String name, Integer minDuration, Integer maxDuration,List<String> tagName);
    List<Playlist> getUserPlaylists(Long id);

    Optional<Playlist> getById(long id);

    Playlist choosePlaylistStrategy(PlaylistDto playlistDto, Playlist playlist, int travelDuration);

    Playlist generateDefaultRankUniqueArtistsPlaylist(Playlist playlist, int travelDuration, List<GenreDto> genreDtoList);

    Playlist generateTopRankSongsNonUniqueArtistPlaylist(Playlist playlist, int travelDuration, List<GenreDto> genreDtoList);

    Playlist generateTopRankSongsUniqueArtistsPlaylist(Playlist playlist, int travelDuration, List<GenreDto> genreDtoList);

    Playlist generateDefaultRankNonUniqueArtistPlaylist(Playlist playlist, int travelDuration, List<GenreDto> genreDtoList);

    Playlist update(User user, Playlist playlistToUpdate, String newName);

    Playlist addSong(User user, Song songToAdd, Playlist playlistToUpdate);

    Playlist deleteSong(User user, Song songToDelete, Playlist playlistToUpdate);

    void createTag(User user, Tag tag, Playlist playlistToUpdate);

    void deleteTag(User user, Tag tag, Playlist playlistToUpdate);

    void delete(User user, long id);

    List<Playlist> getMostRecent();
    List<Playlist> getAll();
}
