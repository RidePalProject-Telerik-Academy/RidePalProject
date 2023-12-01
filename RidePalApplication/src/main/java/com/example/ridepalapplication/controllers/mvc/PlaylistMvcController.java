package com.example.ridepalapplication.controllers.mvc;

import com.example.ridepalapplication.controllers.rest.BingController;
import com.example.ridepalapplication.dtos.PlaylistDto;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.helpers.AuthenticationHelper;
import com.example.ridepalapplication.mappers.PlaylistMapper;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.services.PlaylistService;
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
import java.util.Optional;

@Controller
@RequestMapping("/playlists")
public class PlaylistMvcController {

    private final PlaylistService playlistService;
    private final AuthenticationHelper authenticationHelper;
    private final PlaylistMapper playlistMapper;
    private final BingController bingController;

    @Autowired
    public PlaylistMvcController(PlaylistService playlistService, AuthenticationHelper authenticationHelper,
                                 PlaylistMapper playlistMapper, BingController bingController) {
        this.playlistService = playlistService;
        this.authenticationHelper = authenticationHelper;
        this.playlistMapper = playlistMapper;
        this.bingController = bingController;
    }

    @GetMapping
    public String playlistPage(Model model, @RequestParam(required = false, defaultValue = "") String name) {
        List<Playlist> allPlaylists;

        allPlaylists = playlistService.getAll(0, 9, name, 0, 2147483647, new ArrayList<>());

        model.addAttribute("name", name);
        model.addAttribute("filteredPlaylists", allPlaylists);

        return "PlaylistsView";
    }

    @GetMapping("/generate")
    public String generatePlaylistView(Model model) {
        model.addAttribute("newPlaylist", new PlaylistDto());
        return "GenerateView";
    }

    @PostMapping("/generate")
    public String generatePlaylist(@Valid @ModelAttribute("newPlaylist") PlaylistDto playlistDto,
                                   Authentication authentication, Model model, BindingResult bindingResult) throws ParseException {
        if (bindingResult.hasErrors()) {
            return "GenerateView";
        }

        try {
            User user = authenticationHelper.tryGetUser(authentication);
            Playlist playlist = playlistMapper.fromDto(playlistDto, user);
            int duration = bingController.calculateTravelTime(playlistDto.getLocationDto());
            Playlist generatedPlaylist = playlistService.choosePlaylistStrategy(playlistDto, playlist, duration, playlistDto.getGenreDtoList());
            //TODO: if we save line 69 as a variable, we can return the ID in the SinglePlaylistView?
            return "SinglePlaylistView";
        } catch (EntityNotFoundException e) {
            return "redirect:/";
        }
    }

    @GetMapping("/{id}")
    public String singlePlaylistView(@PathVariable long id, Model model) {
        try {
            Optional<Playlist> playlist = playlistService.getById(id);
            model.addAttribute("singlePlaylist", playlist);
            return "SinglePlaylistView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }


}

