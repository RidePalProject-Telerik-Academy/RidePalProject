package com.example.ridepalapplication.dtos;

import jakarta.validation.constraints.NotNull;

public record SongDto(@NotNull(message = "Song playlist is required.") String title,
                      @NotNull(message = "Song artist is required.") String artist) {

    public SongDto {
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

}
