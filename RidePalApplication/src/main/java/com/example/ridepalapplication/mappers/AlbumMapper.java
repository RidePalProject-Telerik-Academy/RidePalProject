package com.example.ridepalapplication.mappers;

import com.example.ridepalapplication.models.Album;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class AlbumMapper {
    public AlbumMapper() {
    }

    public Album fromJsonToAlbum(JSONObject jsonObject){
        Album album = new Album();
        album.setId((Long) jsonObject.get("id"));
        album.setName((String) jsonObject.get("title"));
        album.setAlbumTrackListUrl((String) jsonObject.get("tracklist"));
        return album;
    }
}
