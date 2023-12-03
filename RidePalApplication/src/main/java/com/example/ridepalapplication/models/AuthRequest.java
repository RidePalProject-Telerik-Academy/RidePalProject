package com.example.ridepalapplication.models;

import jakarta.validation.constraints.NotNull;

public class AuthRequest {

    @NotNull
    private String username;
    @NotNull
    private String password;

    public AuthRequest() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
