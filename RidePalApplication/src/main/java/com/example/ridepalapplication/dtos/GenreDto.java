package com.example.ridepalapplication.dtos;


import jakarta.validation.constraints.NotNull;

public class GenreDto {

    @NotNull(message = "Name is required.")
    String name;
    @NotNull(message = "Percentage is required.")
    int percentage;
    public GenreDto() {
    }

    public GenreDto(String name, int percentage) {
        this.name = name;
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}