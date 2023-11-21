package com.example.ridepalapplication.controllers.rest;

import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.dtos.PlaylistDto;
import com.example.ridepalapplication.dtos.*;
import com.example.ridepalapplication.exceptions.AuthorizationException;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.helpers.AuthenticationHelper;
import com.example.ridepalapplication.helpers.DeezerApiConsumer;
import com.example.ridepalapplication.mappers.PlaylistMapper;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.Song;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.services.PlaylistService;
import com.example.ridepalapplication.services.SongService;
import jakarta.validation.Valid;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/playlists")
public class PlaylistController {
    private final AuthenticationHelper authenticationHelper;
    private final BingController bingController;
    private final PlaylistService playlistService;
    private final PlaylistMapper playlistMapper;
    private final DeezerApiConsumer deezerApiConsumer;
    private final SongService songService;

    @Autowired
    public PlaylistController(AuthenticationHelper authenticationHelper, BingController bingController,
                              PlaylistService playlistService, PlaylistMapper playlistMapper,
                              DeezerApiConsumer deezerApiConsumer, SongService songService) {
        this.authenticationHelper = authenticationHelper;
        this.bingController = bingController;
        this.playlistService = playlistService;
        this.playlistMapper = playlistMapper;
        this.deezerApiConsumer = deezerApiConsumer;
        this.songService = songService;
    }

    @GetMapping
    public List<Playlist> getAll() {
        return playlistService.getAll();
    }

    //TODO: see how to implement filtering - get() - name, creator, duration

    @GetMapping("/{id}")
    public Optional<Playlist> getById(@PathVariable long id) {
        try {
            return playlistService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Playlist generatePlaylist(@RequestBody PlaylistDto playlistDto, @RequestHeader HttpHeaders headers
    ) throws ParseException {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            List<GenreDto> genreList = verifyTotalPercentage(playlistDto);
            Playlist playlist = playlistMapper.fromDto(playlistDto, user);
            int travelDuration = bingController.calculateTravelTime(playlistDto.getLocationDto());
            return playlistService.generatePlaylist(playlist, travelDuration, genreList);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/name")
    public Playlist updateName(@RequestHeader HttpHeaders headers, @PathVariable int id,
                               @Valid @RequestBody UpdatePlaylistNameDto updatePlaylistNameDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Playlist playlistToUpdate = playlistService.getById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Playlist with id %s not found.", id)));
            playlistService.updateName(user, playlistToUpdate, updatePlaylistNameDto.getName());
            return playlistToUpdate;
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}/song")
    public Playlist updateSong(@RequestHeader HttpHeaders headers,
                               @PathVariable int id,
                               @Valid @RequestBody UpdatePlaylistSongDto updatePlaylistSongDto,
                               @RequestParam boolean isAdded) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            Song songToUpdate = songService.getByTitleAndArtist(updatePlaylistSongDto.getTitle(), updatePlaylistSongDto.getArtist());
            Playlist playlistToUpdate = playlistService.getById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Playlist with id %s not found.", id)));
            return playlistService.updateSong(user, songToUpdate, playlistToUpdate, isAdded);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    //TODO: addTag(to Tags table + playlist)
    //TODO: deleteTag(from playlist, not from Tag table)

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable long id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            playlistService.delete(user, id);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    private static List<GenreDto> verifyTotalPercentage(PlaylistDto playlistDto) {
        List<GenreDto> genres = playlistDto.getGenreDtoList();
        int totalGenrePercentage = 0;
        for (GenreDto genreDto : genres) {
            totalGenrePercentage += genreDto.getPercentage();

        }
        if (totalGenrePercentage > 100) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Total genres percentage exceeded!");
        }
        return genres;
    }

    @GetMapping("/artists")
    public void populateArtists() throws ParseException {
        deezerApiConsumer.populateArtists();
    }

    @GetMapping("/albums")
    public void populateAlbums() throws ParseException {
        deezerApiConsumer.populateAlbums();
    }

    @GetMapping("/songs")
    public void populateSongs() throws ParseException {
        deezerApiConsumer.populateSongs();
    }
}
