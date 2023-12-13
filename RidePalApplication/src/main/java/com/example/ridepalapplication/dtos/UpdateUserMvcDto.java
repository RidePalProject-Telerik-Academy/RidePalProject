package com.example.ridepalapplication.dtos;

import jakarta.validation.constraints.Size;

public class UpdateUserMvcDto extends UpdateUserDto {
    @Size(min = 6, max = 16, message = "Password must be between 6 and 16 symbols")
    private String confirmPassword;

    public UpdateUserMvcDto() {
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
