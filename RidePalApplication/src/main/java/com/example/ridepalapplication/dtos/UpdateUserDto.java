package com.example.ridepalapplication.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateUserDto {
    @NotNull(message = "Email is required")
    @Size(min = 6 , max = 24,message = "Email must be between 6 and 24 symbols ")
    private String email;
    @Size(min = 4,max = 32,message = "First Name must be between 4 and 32 symbols")
    private String firstName;
    @Size(min = 4,max = 32,message = "Last Name must be between 4 and 32 symbols")
    private String lastName;
    @Size(min = 6,max = 16,message = "Password must be between 6 and 16 symbols")
    @Pattern(regexp = ".*[!@#%&_*].*",message = "Password must contain a special symbol" )
    private String password;

    public UpdateUserDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
