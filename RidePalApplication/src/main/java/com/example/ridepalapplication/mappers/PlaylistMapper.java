package com.example.ridepalapplication.mappers;

import com.example.ridepalapplication.dtos.PlaylistDto;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.User;
import org.springframework.stereotype.Component;

@Component
public class PlaylistMapper {
    public PlaylistMapper() {
    }
    public Playlist fromDto(PlaylistDto playlistDto, User user){
        Playlist playlist = new Playlist();
        playlist.setName(playlistDto.getName());
        playlist.setCreator(user);
        return playlist;
    }
}
