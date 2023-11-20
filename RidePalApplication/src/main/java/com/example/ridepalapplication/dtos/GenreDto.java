package com.example.ridepalapplication.dtos;

public class GenreDto {

    private String name;
    private int percentage;
    public GenreDto(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
