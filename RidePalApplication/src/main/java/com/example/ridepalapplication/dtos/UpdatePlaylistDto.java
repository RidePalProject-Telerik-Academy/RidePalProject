package com.example.ridepalapplication.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdatePlaylistDto {

    @NotNull(message = "Name is required.")
    @Size(min = 3, max = 16, message = "Name should be between 3 and 16 symbols.")
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
