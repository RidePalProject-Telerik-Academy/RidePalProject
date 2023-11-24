package com.example.ridepalapplication.controllers.rest;

import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.models.Album;
import com.example.ridepalapplication.services.AlbumService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping
    public List<Album> getAll(@RequestParam(required = false,defaultValue = "0")Integer page,
                             @RequestParam(required = false,defaultValue = "10")Integer pageSize,
                             @RequestParam(required = false,defaultValue = "")String albumTitle,
                             @RequestParam(required = false,defaultValue = "")String genre){
        return albumService.findAll(page,pageSize,albumTitle,genre);
    }

    @GetMapping("{id}")
    public Optional<Album> getById(@PathVariable Long id){
        try {
           return albumService.getById(id);
        }
        catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }

}
