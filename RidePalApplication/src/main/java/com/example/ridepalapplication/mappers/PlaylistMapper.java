package com.example.ridepalapplication.mappers;

import com.example.ridepalapplication.dtos.LocationDto;
import com.example.ridepalapplication.dtos.MvcPlaylistDto;
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
    public PlaylistDto fromMvcPlaylistDto(MvcPlaylistDto mvcPlaylistDto) {
        LocationDto locationDto = new LocationDto();
        locationDto.setStartLocation(mvcPlaylistDto.getStartLocation());
        locationDto.setStartAddress(mvcPlaylistDto.getStartAddress());
        locationDto.setEndLocation(mvcPlaylistDto.getEndLocation());
        locationDto.setEndAddress(mvcPlaylistDto.getEndAddress());

        PlaylistDto playlistDto = new PlaylistDto();
        playlistDto.setName(mvcPlaylistDto.getName());
        playlistDto.setLocationDto(locationDto);
        playlistDto.setGenreDtoList(mvcPlaylistDto.getGenres());
        playlistDto.setTopRank(mvcPlaylistDto.isTopRank());
        playlistDto.setUniqueArtists(mvcPlaylistDto.isUniqueArtists());

        return playlistDto;
    }

}
