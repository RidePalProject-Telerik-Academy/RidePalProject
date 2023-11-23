package com.example.ridepalapplication.dtos;

import jakarta.validation.constraints.NotNull;

public class LocationDto {
    @NotNull
    private String startLocation;
    @NotNull
    private String startAddress;
    @NotNull
    private String endLocation;
    @NotNull
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
