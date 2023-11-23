package com.example.ridepalapplication.controllers.rest;

import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.models.Song;
import com.example.ridepalapplication.services.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    private final SongService songService;
    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping
    public List<Song> getAll(@RequestParam(required = false,defaultValue = "0")Integer page,
                             @RequestParam(required = false,defaultValue = "10")Integer pageSize,
                             @RequestParam(required = false,defaultValue = "")String songTitle,
                             @RequestParam(required = false,defaultValue = "")String artist){
        Page<Song> songs = songService.findAll(page,pageSize,songTitle,artist);
      return songs.getContent();
    }

    @GetMapping("{id}")
    public Optional<Song> getById(@PathVariable Long id){
        try{
           return songService.getById(id);
        }catch (EntityNotFoundException e ){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }
}
