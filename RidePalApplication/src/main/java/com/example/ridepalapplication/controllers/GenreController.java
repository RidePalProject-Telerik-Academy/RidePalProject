package com.example.ridepalapplication.controllers;

import com.example.ridepalapplication.helpers.ApiConsumer;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/genres")

public class GenreController {

    private final ApiConsumer apiConsumer;

    public GenreController(ApiConsumer apiConsumer) {

        this.apiConsumer = apiConsumer;
    }

    @GetMapping
    public void getGenres() throws ParseException {
        apiConsumer.getGenres();
    }
    @GetMapping("/test")
    public void getArtist() throws ParseException{
        apiConsumer.getArtist();
    }
}
