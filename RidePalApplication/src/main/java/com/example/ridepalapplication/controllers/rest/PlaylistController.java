package com.example.ridepalapplication.controllers.rest;

import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.dtos.PlaylistDto;
import com.example.ridepalapplication.dtos.*;
import com.example.ridepalapplication.exceptions.AuthorizationException;
import com.example.ridepalapplication.exceptions.EntityDuplicateException;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.helpers.AuthenticationHelper;
import com.example.ridepalapplication.mappers.PlaylistMapper;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.Song;
import com.example.ridepalapplication.models.Tag;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.services.PlaylistService;
import com.example.ridepalapplication.services.SongService;
import com.example.ridepalapplication.services.UserService;
import com.sun.net.httpserver.HttpContext;
import jakarta.validation.Valid;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Encoding;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {
    private final AuthenticationHelper authenticationHelper;
    private final BingController bingController;
    private final PlaylistService playlistService;
    private final PlaylistMapper playlistMapper;
    private final SongService songService;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public PlaylistController(AuthenticationHelper authenticationHelper,
                              BingController bingController,
                              PlaylistService playlistService, PlaylistMapper playlistMapper,
                              SongService songService,
                              UserDetailsService userDetailsService,
                              UserService userService,
                              AuthenticationManager authenticationManager) {
        this.authenticationHelper = authenticationHelper;
        this.bingController = bingController;
        this.playlistService = playlistService;
        this.playlistMapper = playlistMapper;
        this.songService = songService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping
    public List<Playlist> getAll(@RequestParam(required = false, defaultValue = "0") Integer page,
                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                 @RequestParam(required = false, defaultValue = "") String name,
                                 @RequestParam(required = false, defaultValue = "0") Integer minDuration,
                                 @RequestParam(required = false, defaultValue = "2147483647") Integer maxDuration,
                                 @RequestParam(required = false, defaultValue = "") List<String> tagName) {

        return playlistService.getAll(page, pageSize, name, minDuration, maxDuration, tagName);
    }

    @GetMapping("/{id}")
    public Optional<Playlist> getById(@PathVariable long id) {
        try {
            return playlistService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Playlist generatePlaylist(@RequestBody PlaylistDto playlistDto, @RequestHeader HttpHeaders headers) throws ParseException {

        try {
            String authorization = headers.get("Authorization").get(0);
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            // credentials = username:password
            final String[] values = credentials.split(":", 2);
            final String username = values[0];
            User user = userService.getByUsername(username);
            List<GenreDto> genreList = verifyTotalPercentage(playlistDto);
            Playlist playlist = playlistMapper.fromDto(playlistDto, user);
            int travelDuration = bingController.calculateTravelTime(playlistDto.getLocationDto());
            return playlistService.generatePlaylist(playlist, travelDuration, genreList);

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
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

    @PostMapping("/{id}/song")
    public Playlist addSong(@RequestHeader HttpHeaders headers,
                            @PathVariable int id,
                            @Valid @RequestBody UpdatePlaylistSongDto updatePlaylistSongDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Song songToUpdate = songService.getByTitleAndArtist(updatePlaylistSongDto.getTitle(), updatePlaylistSongDto.getArtist());
            Playlist playlistToUpdate = playlistService.getById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Playlist with id %s not found.", id)));
            return playlistService.addSong(user, songToUpdate, playlistToUpdate);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}/song")
    public Playlist deleteSong(@RequestHeader HttpHeaders headers,
                               @PathVariable int id,
                               @Valid @RequestBody UpdatePlaylistSongDto updatePlaylistSongDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Song songToUpdate = songService.getByTitleAndArtist(updatePlaylistSongDto.getTitle(), updatePlaylistSongDto.getArtist());
            Playlist playlistToUpdate = playlistService.getById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Playlist with id %s not found.", id)));
            return playlistService.deleteSong(user, songToUpdate, playlistToUpdate);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/{id}/tag")
    public Playlist createTag(@RequestHeader HttpHeaders headers, @PathVariable int id, @RequestBody Tag tag) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Playlist playlistToUpdate = playlistService.getById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Playlist with id %s not found.", id)));

            playlistService.createTag(user, tag, playlistToUpdate);
            return playlistToUpdate;
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}/tag")
    public Playlist deleteTag(@RequestHeader HttpHeaders headers, @PathVariable int id, @RequestBody Tag tag) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Playlist playlistToUpdate = playlistService.getById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Playlist with id %s not found.", id)));

            playlistService.deleteTag(user, tag, playlistToUpdate);
            return playlistToUpdate;
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

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
}
