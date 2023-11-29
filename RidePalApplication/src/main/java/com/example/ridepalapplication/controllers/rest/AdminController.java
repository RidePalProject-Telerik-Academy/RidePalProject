package com.example.ridepalapplication.controllers.rest;

import com.example.ridepalapplication.helpers.DeezerApiConsumer;
import com.example.ridepalapplication.models.SynchronizationDetails;
import com.example.ridepalapplication.repositories.SynchronizationDetailsRepository;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final DeezerApiConsumer apiConsumer;
    SynchronizationDetailsRepository synchronizationDetailsRepository;

    @Autowired
    public AdminController(DeezerApiConsumer apiConsumer, SynchronizationDetailsRepository synchronizationDetailsRepository) {
        this.apiConsumer = apiConsumer;
        this.synchronizationDetailsRepository = synchronizationDetailsRepository;
    }

    @GetMapping("/artists")
    public void populateArtists() throws ParseException {
        apiConsumer.populateArtists();
    }

    @GetMapping("/genres")
    public void populateGenres() throws ParseException {
        apiConsumer.populateGenres();
    }

    @Scheduled(fixedRateString = "${sync.key}")
    public void synchronizeGenres() {
        try {
            apiConsumer.populateGenres();
            updateSynchronizationDetails(true);
        } catch (ParseException e) {
            updateSynchronizationDetails(false);
        }
    }

    private void updateSynchronizationDetails(boolean success) {

        LocalDateTime lastSyncTime = LocalDateTime.now();
        SynchronizationDetails synchronizationDetails = new SynchronizationDetails();

        if (success) {
            synchronizationDetails.setStatus("success");
            synchronizationDetails.setSyncTime(lastSyncTime);
            synchronizationDetailsRepository.save(synchronizationDetails);
        } else {
            synchronizationDetails.setStatus("failure");
            synchronizationDetails.setSyncTime(lastSyncTime);
            synchronizationDetailsRepository.save(synchronizationDetails);
        }
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
