package com.example.ridepalapplication.dtos;

import java.util.List;

public class PlaylistDto {
    private  LocationDto locationDto;
    private  List<GenreDto> genreDtoList;
    private String name;



    public PlaylistDto() {
    }

    public LocationDto getLocationDto() {
        return locationDto;
    }

    public void setLocationDto(LocationDto locationDto) {
        this.locationDto = locationDto;
    }

    public List<GenreDto> getGenreDtoList() {
        return genreDtoList;
    }

    public void setGenreDtoList(List<GenreDto> genreDtoList) {
        this.genreDtoList = genreDtoList;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
