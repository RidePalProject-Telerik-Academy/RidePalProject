package com.example.ridepalapplication.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PlaylistDto extends UpdatePlaylistDto {

    @NotNull(message = "Location is required.")
    private LocationDto locationDto;

    @NotNull(message = "Genre is required.")
    private List<GenreDto> genreDtoList;

    public PlaylistDto() {
    }

    public LocationDto getLocationDto() {
        return locationDto;
    }

    public List<GenreDto> getGenreDtoList() {
        return genreDtoList;
    }

}
