package com.example.ridepalapplication.controllers.rest;

import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.dtos.LocationDto;
import com.example.ridepalapplication.helpers.AuthenticationHelper;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.services.PlaylistService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/playlists")
public class PlaylistController {
    private final AuthenticationHelper authenticationHelper;
    private final BingController bingController;
    private final PlaylistService playlistService;

    @Autowired
    public PlaylistController(AuthenticationHelper authenticationHelper, BingController bingController, PlaylistService playlistService) {
        this.authenticationHelper = authenticationHelper;
        this.bingController = bingController;
        this.playlistService = playlistService;
    }

    @PostMapping
    public Playlist generatePlaylist(@RequestBody LocationDto locationDto,@RequestBody List<GenreDto> genreDtos, @RequestHeader HttpHeaders headers
    ) throws ParseException {
        int travelDuration = bingController.calculateTravelTime(locationDto);
        User user = authenticationHelper.tryGetUser(headers);
        return playlistService.generatePlaylist(travelDuration,genreDtos,user);


    }
}
