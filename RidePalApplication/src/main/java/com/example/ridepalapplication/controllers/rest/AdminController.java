package com.example.ridepalapplication.controllers.rest;

import com.example.ridepalapplication.helpers.DeezerApiConsumer;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final DeezerApiConsumer apiConsumer;
    @Autowired
    public AdminController(DeezerApiConsumer apiConsumer) {
        this.apiConsumer = apiConsumer;
    }
    @GetMapping("/artists")
    public void populateArtists() throws ParseException {
        apiConsumer.populateArtists();
    }
    @GetMapping("/genres")
    public void populateGenres() throws ParseException {
        apiConsumer.populateGenres();
    }

    @GetMapping("/albums")
    public void populateAlbums() throws ParseException {
        apiConsumer.populateAlbums();
    }

    @GetMapping("/songs")
    public void populateSongs() throws ParseException {
        apiConsumer.populateSongs();
    }
}
