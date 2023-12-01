package com.example.ridepalapplication.controllers.mvc;

import com.example.ridepalapplication.controllers.rest.BingController;
import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.dtos.MvcPlaylistDto;
import com.example.ridepalapplication.dtos.PlaylistDto;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.helpers.AuthenticationHelper;
import com.example.ridepalapplication.mappers.PlaylistMapper;
import com.example.ridepalapplication.models.Genre;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.repositories.GenreRepository;
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
    public String playlistPage(Model model, @RequestParam(required = false, defaultValue = "") String name,
                               @RequestParam(required = false, defaultValue = "0") Integer page,
                               @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                               @RequestParam(required = false, defaultValue = "0") Integer minDuration,
                               @RequestParam(required = false, defaultValue = "2147483647") Integer maxDuration) {
        List<Playlist> allPlaylists;

        allPlaylists = playlistService.getAll(page, pageSize, name, minDuration, maxDuration, new ArrayList<>());

        model.addAttribute("name", name);
        model.addAttribute("filteredPlaylists", allPlaylists);

        return "PlaylistsView";
    }

    @GetMapping("/generate")
    public String generatePlaylistView(Model model) {
        //take from DB map to GenreDto with loop
        GenreDto pop = new GenreDto("Pop", 0);
        GenreDto rock = new GenreDto("Rock", 0);
        GenreDto rap = new GenreDto("Rap/Hip Hop", 0);

        // List<GenreDto> genreList = PlaylistHelper.verifyTotalPercentage(playlistDto);

        List<GenreDto> genres = List.of(pop, rock, rap);
        MvcPlaylistDto mvcPlaylistDto = new MvcPlaylistDto();
        mvcPlaylistDto.setGenres(genres);
        model.addAttribute("newPlaylist", mvcPlaylistDto);
        return "GenerateView";
    }

    @PostMapping("/generate")
    public String generatePlaylist(@Valid @ModelAttribute("newPlaylist") MvcPlaylistDto mvcPlaylistDto,
                                   Authentication authentication, BindingResult bindingResult) throws ParseException {
        if (bindingResult.hasErrors()) {
            return "GenerateView";
        }

        try {
            PlaylistDto playlistDto = playlistMapper.fromMvcPlaylistDto(mvcPlaylistDto);
            User user = authenticationHelper.tryGetUser(authentication);
            Playlist playlist = playlistMapper.fromDto(playlistDto, user);
            int duration = bingController.calculateTravelTime(playlistDto.getLocationDto());
            playlist = playlistService.choosePlaylistStrategy(playlistDto, playlist, duration, playlistDto.getGenreDtoList());
            return "redirect:/playlists/" + playlist.getId();
        } catch (EntityNotFoundException e) {
            return "redirect:/";
        }
    }

    @GetMapping("/{id}")
    public String singlePlaylistView(@PathVariable long id, Model model) {
        try {
            Playlist playlist = playlistService.getById(id).orElseThrow();
            model.addAttribute("singlePlaylist", playlist);
            return "SinglePlaylistView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }


}

