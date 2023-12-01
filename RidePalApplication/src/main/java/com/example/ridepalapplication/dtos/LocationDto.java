package com.example.ridepalapplication.dtos;

import jakarta.validation.constraints.NotNull;

public class LocationDto {

    @NotNull(message = "Start location is required.")
    private String startLocation;
    @NotNull(message = "Start address is required.")
    private String startAddress;
    @NotNull(message = "End location is required.")
    private String endLocation;
    @NotNull(message = "End address is required.")
    private String endAddress;

    public LocationDto() {
    }

    public String getStartLocation() {
        return startLocation.replaceAll(" ", "+");
    }

    public String getStartAddress() {
        return startAddress.replaceAll(" ", "+");
    }

    public String getEndLocation() {
        return endLocation.replaceAll(" ", "+");
    }

    public String getEndAddress() {
        return endAddress.replaceAll(" ", "+");
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }
}
