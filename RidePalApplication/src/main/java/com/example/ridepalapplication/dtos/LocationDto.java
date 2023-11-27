package com.example.ridepalapplication.dtos;

import jakarta.validation.constraints.NotNull;

public record LocationDto(@NotNull(message = "Start location is required.") String startLocation,
                          @NotNull(message = "Start address is required.") String startAddress,
                          @NotNull(message = "End location is required.") String endLocation,
                          @NotNull(message = "End address is required.") String endAddress) {

    public LocationDto {
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

}
