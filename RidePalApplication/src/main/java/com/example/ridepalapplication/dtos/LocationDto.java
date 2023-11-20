package com.example.ridepalapplication.dtos;

import jakarta.validation.constraints.NotNull;

public class LocationDto {
    @NotNull
    private String startLocation;

    @NotNull
    private String endLocation;

    public LocationDto() {
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }
}
