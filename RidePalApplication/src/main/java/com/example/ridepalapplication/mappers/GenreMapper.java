package com.example.ridepalapplication.mappers;

import com.example.ridepalapplication.models.Genre;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GenreMapper {
    public GenreMapper() {
    }
    public Genre fromMapToGenre(Map<String, Object> map) {
        String genreName = (String) map.get("name");
        Long id = (Long) map.get("id");
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(genreName);
        return genre;
    }
}
