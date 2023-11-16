package com.example.ridepalapplication.helpers;


import com.example.ridepalapplication.mappers.GenreMapper;
import com.example.ridepalapplication.models.Genre;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class ApiConsumer {
    private static final String DEEZER_GENRE_URL = "https://api.deezer.com/genre";
    private final RestTemplate restTemplate;
    private final JSONParser parser;


    private final GenreMapper genreMapper;

    public ApiConsumer(RestTemplate restTemplate, JSONParser parser, GenreMapper genreMapper) {
        this.restTemplate = restTemplate;
        this.parser = parser;


        this.genreMapper = genreMapper;
    }

    public void getGenres() throws ParseException {
        String url = DEEZER_GENRE_URL;
        String response = restTemplate.getForObject(url, String.class);
        JSONObject object = (JSONObject) parser.parse(response);
        JSONArray array = (JSONArray) object.get("data");
        Map<String, Object> dataBaseEntity = new HashMap<>();

        for (int i = 1; i < array.size(); i++) {
            JSONObject dataObject = (JSONObject) array.get(i);
            for (Object k : dataObject.keySet()) {
                String key = (String) k;
                Object value = dataObject.get(key);
                dataBaseEntity.put(key, value);
            }
            Genre genre = genreMapper.fromMapToGenre(dataBaseEntity);
            dataBaseEntity.clear();
        }
    }

    }

