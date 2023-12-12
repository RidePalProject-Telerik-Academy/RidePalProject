package com.example.ridepalapplication.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MvcPlaylistDto {
    @NotNull(message = "Name is required.")
    String name;
    @NotNull(message = "Start location is required.")
    private String startLocation;
    private String startAddress;
    @NotNull(message = "End location is required.")
    private String endLocation;
    private String endAddress;
    private List<GenreDto> genres;
    private boolean topRank;
    private boolean uniqueArtists;

    public MvcPlaylistDto() {
        this.genres = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public List<GenreDto> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreDto> genres) {
        this.genres = genres;
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
