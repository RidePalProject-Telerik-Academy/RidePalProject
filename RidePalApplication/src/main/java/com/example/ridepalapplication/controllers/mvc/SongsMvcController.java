package com.example.ridepalapplication.controllers.mvc;

import com.example.ridepalapplication.helpers.AuthenticationHelper;
import com.example.ridepalapplication.models.Song;
import com.example.ridepalapplication.services.SongService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/songs")
public class SongsMvcController {

    private final SongService songService;
    private final AuthenticationHelper authenticationHelper;
    @Autowired
    public SongsMvcController(SongService songService, AuthenticationHelper authenticationHelper) {
        this.songService = songService;
        this.authenticationHelper = authenticationHelper;
    }


    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(){
        return authenticationHelper.isAuthenticated();
    }

    @GetMapping
    public String getSongsPage(Model model, @RequestParam(required = false, defaultValue = "") String songTitle,
                               @RequestParam(required = false, defaultValue = "0") Integer page,
                               @RequestParam(required = false, defaultValue = "50") Integer pageSize,
                               @RequestParam(required = false,defaultValue = "")String artist){
        model.addAttribute("title",songTitle);

        List<Song> filteredSongs = songService.findAll(page,pageSize,songTitle,artist);

        int pages = (songService.findAll().size())/pageSize;
        model.addAttribute("songs",filteredSongs);
        model.addAttribute("page",page);
        model.addAttribute("pageSize",pages);


        return "SongsView";
    }
}
