package com.example.ridepalapplication.dtos;

import jakarta.validation.constraints.NotNull;

public class UpdatePlaylistDto {

    @NotNull(message = "Name is required.")
    private String name;

    public UpdatePlaylistDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
