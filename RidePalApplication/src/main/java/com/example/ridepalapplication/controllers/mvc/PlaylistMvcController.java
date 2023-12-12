package com.example.ridepalapplication.controllers.mvc;

import com.example.ridepalapplication.controllers.rest.BingController;
import com.example.ridepalapplication.dtos.*;
import com.example.ridepalapplication.exceptions.AuthorizationException;
import com.example.ridepalapplication.exceptions.EntityDuplicateException;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.helpers.AuthenticationHelper;
import com.example.ridepalapplication.helpers.PlaylistHelper;
import com.example.ridepalapplication.mappers.PlaylistMapper;
import com.example.ridepalapplication.mappers.TagMapper;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.Song;
import com.example.ridepalapplication.models.Tag;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.services.PlaylistService;
import com.example.ridepalapplication.services.SongService;
import jakarta.validation.Valid;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/playlists")
public class PlaylistMvcController {

    private final PlaylistService playlistService;
    private final SongService songService;
    private final AuthenticationHelper authenticationHelper;
    private final PlaylistMapper playlistMapper;
    private final TagMapper tagMapper;
    private final BingController bingController;

    @Autowired
    public PlaylistMvcController(PlaylistService playlistService, SongService songService, AuthenticationHelper authenticationHelper,
                                 PlaylistMapper playlistMapper, TagMapper tagMapper, BingController bingController) {
        this.playlistService = playlistService;
        this.songService = songService;
        this.authenticationHelper = authenticationHelper;
        this.playlistMapper = playlistMapper;
        this.tagMapper = tagMapper;
        this.bingController = bingController;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated() {
        return authenticationHelper.isAuthenticated();
    }


    @GetMapping
    public String playlistPage(Model model, @RequestParam(required = false, defaultValue = "") String name,
                               @RequestParam(required = false, defaultValue = "0") Integer page,
                               @RequestParam(required = false, defaultValue = "9") Integer pageSize,
                               @RequestParam(required = false, defaultValue = "0") Integer minDuration,
                               @RequestParam(required = false, defaultValue = "2147483647") Integer maxDuration,
                               @RequestParam(required = false, defaultValue = "") List<String> genres) {


        List<Playlist> recentPlaylists = playlistService.getMostRecent();
        List<Playlist> filteredList = playlistService.getAll(page, pageSize, name, minDuration, maxDuration, genres);
        int pages = (playlistService.getAll().size()) / pageSize;

        model.addAttribute("filteredMostRecent", recentPlaylists);
        model.addAttribute("minDuration", minDuration);
        model.addAttribute("maxDuration", maxDuration);
        model.addAttribute("genres", genres);
        model.addAttribute("page", page);
        model.addAttribute("maxDuration", maxDuration);
        model.addAttribute("pageSize", pages);
        model.addAttribute("filteredPlaylists", filteredList);
        model.addAttribute("filteredPlaylists", filteredList);


        return "PlaylistsView";
    }

    @GetMapping("/generate")
    public String generatePlaylistView(Model model) {
        GenreDto pop = new GenreDto("Pop", 0);
        GenreDto rock = new GenreDto("Rock", 0);
        GenreDto rap = new GenreDto("Rap/Hip Hop", 0);

        List<GenreDto> genres = List.of(pop, rock, rap);
        MvcPlaylistDto mvcPlaylistDto = new MvcPlaylistDto();
        mvcPlaylistDto.setGenres(genres);
        model.addAttribute("newPlaylist", mvcPlaylistDto);
        return "GenerateView";
    }

    @PostMapping("/generate")
    public String generatePlaylist(@Valid @ModelAttribute("newPlaylist") MvcPlaylistDto mvcPlaylistDto,
                                   Authentication authentication, BindingResult bindingResult, Model model) throws ParseException {
        if (bindingResult.hasErrors()) {
            return "GenerateView";
        }

        try {
            PlaylistHelper.verifyTotalGenrePercentage(mvcPlaylistDto.getGenres());
            PlaylistDto playlistDto = playlistMapper.fromMvcPlaylistDto(mvcPlaylistDto);
            User user = authenticationHelper.tryGetUser(authentication);
            Playlist playlist = playlistMapper.fromDto(playlistDto, user);
            int duration = bingController.calculateTravelTime(playlistDto.getLocationDto());
            playlist = playlistService.choosePlaylistStrategy(playlistDto, playlist, duration, playlistDto.getGenreDtoList());
            return "redirect:/playlists/" + playlist.getId();
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            model.addAttribute("statusCode", HttpStatus.CONFLICT.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}")
    public String singlePlaylistView(@PathVariable long id, Model model, Authentication authentication) {
        String username;
        try {
            username = authentication.getName();
        } catch (NullPointerException e) {
            username = "";
        }
        try {
            Playlist playlist = playlistService.getById(id).orElseThrow();
            model.addAttribute("singlePlaylist", playlist);
            playlist.getSongs().forEach(song -> model.addAttribute(song.getTitle(), song));
            model.addAttribute("user", playlist.getCreator());
            model.addAttribute("username", username);
            model.addAttribute("newTag", new TagDto());
            model.addAttribute("tags", playlist.getTags());
            model.addAttribute("updatePlaylistDto", new UpdatePlaylistDto());
            return "SinglePlaylistView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
    
        @PostMapping("/{id}/update")
        public String updatePlaylist(@PathVariable long id,
                                     Authentication authentication,
                                     @Valid @ModelAttribute("updatePlaylistDto") UpdatePlaylistDto updatePlaylistDto,
                                     BindingResult bindingResult, Model model) {

            if (bindingResult.hasErrors()) {
                return "ErrorView";
            }

            try {
                User user = authenticationHelper.tryGetUser(authentication);
                Playlist playlist = playlistService.getById(id).orElseThrow();
                playlistService.update(user, playlist, updatePlaylistDto.getName());
                return "redirect:/playlists/" + id;
            } catch (AuthorizationException e) {
                model.addAttribute("error", e.getMessage());
                bindingResult.rejectValue("name", "name_error", e.getMessage());
                return "ErrorView";
            }

        }

    @GetMapping("/{id}/delete")
    public String deletePlaylist(@PathVariable long id,
                                 Authentication authentication) {

        User user = authenticationHelper.tryGetUser(authentication);
        playlistService.delete(user, id);

        return "redirect:/users/myProfile";
    }

    @PostMapping("/{id}/tags")
    public String createTag(@PathVariable long id,
                            @Valid @ModelAttribute("newTag") TagDto tagDto,
                            Authentication authentication, Model model) {

        try {
            User user = authenticationHelper.tryGetUser(authentication);
            Playlist playlist = playlistService.getById(id).orElseThrow();

            Tag tag = tagMapper.fromDto(tagDto);
            playlistService.createTag(user, tag, playlist);
            return "redirect:/playlists/" + id;
        } catch (EntityDuplicateException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/tag")
    public String deleteTag(@PathVariable long id,
                            @Valid @ModelAttribute("newTag") TagDto tagDto,
                            Authentication authentication) {

        try {
            User user = authenticationHelper.tryGetUser(authentication);
            Playlist playlist = playlistService.getById(id).orElseThrow();

            Tag tag = tagMapper.fromDto(tagDto);
            playlistService.deleteTag(user, tag, playlist);
            return "redirect:/playlists/" + id;
        } catch (EntityDuplicateException e) {
            return "ErrorView";
            //TODO: fix error
        }
    }

    @PostMapping("/{playlistId}/songs/{songId}/delete")
    public String deleteSong(@PathVariable long playlistId,
                             @PathVariable long songId,
                             Authentication authentication) {

        User user = authenticationHelper.tryGetUser(authentication);

        Playlist playlist = playlistService.getById(playlistId).orElseThrow();
        Song song = songService.getById(songId).orElseThrow();

        playlistService.deleteSong(user, song, playlist);

        return "redirect:/playlists/" + playlistId;

    }

}

