package com.example.ridepalapplication.services;

import com.example.ridepalapplication.models.Artist;
import com.example.ridepalapplication.models.Song;

public interface SongService {

    Song getByTitleAndArtist(String name, String artist);

}
