package com.example.ridepalapplication.mappers;

import com.example.ridepalapplication.models.Genre;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GenreMapper {
    public GenreMapper() {
    }
    public Genre fromMapToGenre(JSONObject jsonObject) {
        String genreName = (String) jsonObject.get("name");
        Long id = (Long) jsonObject.get("id");
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(genreName);
        return genre;
    }
}
