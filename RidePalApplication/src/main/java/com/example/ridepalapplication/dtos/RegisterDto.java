package com.example.ridepalapplication.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterDto {

    @NotNull(message = "Username cannot be empty.")
    @Size(min = 4,message = "Username must be at least 4 symbols long.")
    private String username;
    @NotNull(message = "First name is required.")
    @Size(min = 4, max = 32, message = "First name must be between 4 and 32 symbols.")
    private String firstName;
    @NotNull(message = "Last name is required.")
    @Size(min = 4, max = 32, message = "Last name must be between 4 and 32 symbols.")
    private String lastName;
    @NotNull(message = "Email is required.")
    @Size(min = 6, max = 320, message = "Email must be between 6 and 320 symbols.")
    private String email;
    @NotNull(message = "Password is required.")
    @Size(min = 6, max = 16, message = "Password must be between 6 and 16 symbols")
    private String password;
    @NotNull(message = "Password confirmation is required.")
    private String confirmPassword;

    public RegisterDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
