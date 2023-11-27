package com.example.ridepalapplication.dtos;
import jakarta.validation.constraints.NotNull;

public record TagDto(@NotNull(message = "Tag playlist is required.") String tagName) {
    public TagDto {
    }

    public String getTagName() {
        return tagName;
    }

}
