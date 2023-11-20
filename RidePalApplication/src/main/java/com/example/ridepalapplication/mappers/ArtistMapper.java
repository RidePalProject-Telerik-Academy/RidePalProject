package com.example.ridepalapplication.mappers;

import com.example.ridepalapplication.models.Artist;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class ArtistMapper {
    public ArtistMapper() {
    }
    public Artist fromJsonToArtist(JSONObject jsonObject){
        Artist artist = new Artist();
        artist.setId((Long) jsonObject.get("id"));
        artist.setName((String) jsonObject.get("name"));
        artist.setArtistTrackListUrl((String) jsonObject.get("tracklist"));
        return artist;
    }
}
