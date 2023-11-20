package com.example.ridepalapplication.mappers;

import com.example.ridepalapplication.models.Song;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class SongMapper {
    public SongMapper() {
    }
    public Song fromJsonToSong(JSONObject jsonObject){
        Song song = new Song();
        song.setId((Long) jsonObject.get("id"));
        song.setTitle((String) jsonObject.get("title"));
        song.setLink((String) jsonObject.get("link"));
        song.setDuration((Long) jsonObject.get("duration"));
        song.setRank((Long) jsonObject.get("rank"));
        song.setPreviewUrl((String) jsonObject.get("preview"));
        return song;
    }
}
