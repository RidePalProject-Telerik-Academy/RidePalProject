package com.example.ridepalapplication.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDto extends UpdateUserDto{
    @NotNull(message = "Username cannot be empty.")
    @Size(min = 4,message = "Username must be at least 4 symbols long.")
    private String username;

    public UserDto() {
    }

    public String getUsername() {
        return username;
    }
}
