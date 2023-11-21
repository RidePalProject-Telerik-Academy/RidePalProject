package com.example.ridepalapplication.dtos;

import jakarta.validation.constraints.NotNull;

public class UpdatePlaylistNameDto {

    @NotNull(message = "Playlist name is required")
    String name;

    public UpdatePlaylistNameDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
