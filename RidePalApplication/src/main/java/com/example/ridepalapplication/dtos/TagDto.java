package com.example.ridepalapplication.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TagDto {

    @NotEmpty(message = "Tag name is required.")
    @Size(min = 2, max = 16, message = "Tag name must be between 2 and 16 symbols.")
    private String tagName;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
