package com.example.ridepalapplication.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PlaylistDto extends UpdatePlaylistDto {

    @NotNull(message  = "Location is required.")
    private LocationDto locationDto;

    @NotNull(message = "Genre is required.")
    private List<GenreDto> genreDtoList;
    private boolean topRank;

    private boolean uniqueArtists;

    public PlaylistDto() {
    }

    public LocationDto getLocationDto() {
        return locationDto;
    }
    public List<GenreDto> getGenreDtoList() {
        return genreDtoList;
    }
    public boolean topRank() {
        return topRank;
    }
    public boolean uniqueArtists() {
        return uniqueArtists;
    }

    public void setLocationDto(LocationDto locationDto) {
        this.locationDto = locationDto;
    }

    public void setGenreDtoList(List<GenreDto> genreDtoList) {
        this.genreDtoList = genreDtoList;
    }

    public boolean isTopRank() {
        return topRank;
    }

    public void setTopRank(boolean topRank) {
        this.topRank = topRank;
    }

    public boolean isUniqueArtists() {
        return uniqueArtists;
    }

    public void setUniqueArtists(boolean uniqueArtists) {
        this.uniqueArtists = uniqueArtists;
    }
}
