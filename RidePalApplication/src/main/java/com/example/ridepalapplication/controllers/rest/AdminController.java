package com.example.ridepalapplication.controllers.rest;

import com.example.ridepalapplication.consumers.DeezerApiConsumer;
import com.example.ridepalapplication.helpers.AuthenticationHelper;
import com.example.ridepalapplication.services.SyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/admins")
@Tag(name = "Administration")
public class AdminController {

    private final DeezerApiConsumer apiConsumer;
    private final SyncService syncService;

    @Autowired
    public AdminController(DeezerApiConsumer apiConsumer, SyncService syncService) {
        this.apiConsumer = apiConsumer;
        this.syncService = syncService;

    }

    @Operation(summary = "Connect to Deezer API to populate artists in the database")
    @GetMapping("/artists")
    public void populateArtists() throws ParseException {

        apiConsumer.populateArtists();

    }

    @Operation(summary = "Connect to Deezer API to populate genres in the database")
    @GetMapping("/genres")
    public void populateGenres() throws ParseException {
        syncService.synchronize();
    }

    @Scheduled(fixedRateString = "${sync.key}")
    public void synchronizeGenres() throws ParseException {
        syncService.synchronize();
    }

    @Operation(summary = "Connect to Deezer API to populate albums in the database")
    @GetMapping("/albums")
    public void populateAlbums() throws ParseException {
        apiConsumer.populateAlbums();
    }

    @Operation(summary = "Connect to Deezer API to populate songs in the database")
    @GetMapping("/songs")
    public void populateSongs() throws ParseException {
        apiConsumer.populateSongs();
    }
}
