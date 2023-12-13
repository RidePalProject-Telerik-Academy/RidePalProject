package com.example.ridepalapplication.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(hidden = true)
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
