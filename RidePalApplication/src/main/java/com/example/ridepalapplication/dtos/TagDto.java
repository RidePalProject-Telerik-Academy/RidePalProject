package com.example.ridepalapplication.dtos;

import jakarta.validation.constraints.NotNull;

public class TagDto {

    @NotNull(message = "Tag playlist is required.")
    private String tagName;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
