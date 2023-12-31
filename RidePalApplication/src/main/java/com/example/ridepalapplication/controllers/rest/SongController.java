package com.example.ridepalapplication.controllers.rest;

import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.models.Song;
import com.example.ridepalapplication.services.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/songs")
@Tag(name = "Songs")
public class SongController {

    private final SongService songService;
    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @Operation(summary = "Retrieve all songs")
    @GetMapping
    public List<Song> getAll(@RequestParam(required = false,defaultValue = "0")Integer page,
                             @RequestParam(required = false,defaultValue = "10")Integer pageSize,
                             @RequestParam(required = false,defaultValue = "")String songTitle,
                             @RequestParam(required = false,defaultValue = "")String artist){

      return songService.findAll(page,pageSize,songTitle,artist);
    }

    @Operation(summary = "Retrieve single song by id")
    @GetMapping("{id}")
    public Optional<Song> getById(@PathVariable Long id){
        try{
           return songService.getById(id);
        }catch (EntityNotFoundException e ){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }
}
