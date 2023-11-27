package com.example.ridepalapplication.dtos;

import jakarta.validation.constraints.NotNull;

public record GenreDto(@NotNull(message = "Genre playlist is required.") String name, int percentage) {

    public GenreDto {
    }

    public String getName() {
        return name;
    }

    public int getPercentage() {
        return percentage;
    }
}