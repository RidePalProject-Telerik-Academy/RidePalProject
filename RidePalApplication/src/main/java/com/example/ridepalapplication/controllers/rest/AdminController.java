package com.example.ridepalapplication.controllers.rest;

import com.example.ridepalapplication.consumers.DeezerApiConsumer;
import com.example.ridepalapplication.services.SyncService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final DeezerApiConsumer apiConsumer;
    private final SyncService syncService;
    @Autowired
    public AdminController(DeezerApiConsumer apiConsumer, SyncService syncService) {
        this.apiConsumer = apiConsumer;
        this.syncService = syncService;
    }

    @GetMapping("/artists")
    public void populateArtists() throws ParseException {
        apiConsumer.populateArtists();
    }

    @GetMapping("/genres")
    public void populateGenres() throws ParseException {
        syncService.synchronize();
    }

    @Scheduled(fixedRateString = "${sync.key}")
    public void synchronizeGenres() throws ParseException {
        syncService.synchronize();
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
