package com.example.ridepalapplication.controllers.rest;

import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.dtos.LocationDto;
import com.example.ridepalapplication.dtos.PlaylistDto;
import com.example.ridepalapplication.exceptions.AuthorizationException;
import com.example.ridepalapplication.helpers.AuthenticationHelper;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.services.PlaylistService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public Playlist generatePlaylist(@RequestBody PlaylistDto playlistDto, @RequestHeader HttpHeaders headers
    ) throws ParseException {
        verifyTotalPercentage(playlistDto);
        int travelDuration = bingController.calculateTravelTime(playlistDto.getLocationDto());
       try {
           User user = authenticationHelper.tryGetUser(headers);
           Playlist playlist = new Playlist();
           playlist.setName(playlistDto.getName());
           playlist.setCreator(user);
           return playlistService.generatePlaylist(playlist, travelDuration, playlistDto.getGenreDtoList());
       }
       catch (AuthorizationException e){
           throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
       }
    }
    private static void verifyTotalPercentage(PlaylistDto playlistDto) {
        List<GenreDto> genres = playlistDto.getGenreDtoList();
        int totalGenrePercentage = 0;
        for (GenreDto genreDto : genres) {
            totalGenrePercentage += genreDto.getPercentage();
        }
        if(totalGenrePercentage > 100){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Total genres percentage exceeded !");
        }
    }
}
