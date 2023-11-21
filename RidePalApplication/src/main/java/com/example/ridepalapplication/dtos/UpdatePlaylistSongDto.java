package com.example.ridepalapplication.dtos;

import jakarta.validation.constraints.NotNull;

public class UpdatePlaylistSongDto {

    @NotNull(message = "Song name is required.")
    String title;
    @NotNull(message = "Song artist is required.")
    String artist;

    public UpdatePlaylistSongDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
