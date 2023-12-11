package com.example.ridepalapplication.controllers.rest;

import com.example.ridepalapplication.helpers.DeezerApiConsumer;
import com.example.ridepalapplication.models.SynchronizationDetails;
import com.example.ridepalapplication.repositories.SynchronizationDetailsRepository;
import com.example.ridepalapplication.services.SynchronizationService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final DeezerApiConsumer apiConsumer;
    private final SynchronizationService synchronizationService;
    @Autowired
    public AdminController(DeezerApiConsumer apiConsumer,SynchronizationService synchronizationService) {
        this.apiConsumer = apiConsumer;
        this.synchronizationService = synchronizationService;
    }

    @GetMapping("/artists")
    public void populateArtists() throws ParseException {
        apiConsumer.populateArtists();
    }

    @GetMapping("/genres")
    public void populateGenres() throws ParseException {
        synchronizationService.synchronize();
    }

    @Scheduled(fixedRateString = "${sync.key}")
    public void synchronizeGenres() throws ParseException {
        synchronizationService.synchronize();
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
