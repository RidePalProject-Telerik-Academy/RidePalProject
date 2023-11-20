package com.example.ridepalapplication.helpers;


import com.example.ridepalapplication.mappers.AlbumMapper;
import com.example.ridepalapplication.mappers.ArtistMapper;
import com.example.ridepalapplication.mappers.GenreMapper;
import com.example.ridepalapplication.mappers.SongMapper;
import com.example.ridepalapplication.models.Album;

import com.example.ridepalapplication.models.Artist;
import com.example.ridepalapplication.models.Genre;
import com.example.ridepalapplication.models.Song;
import com.example.ridepalapplication.repositories.AlbumRepository;
import com.example.ridepalapplication.repositories.ArtistRepository;
import com.example.ridepalapplication.repositories.GenreRepository;
import com.example.ridepalapplication.repositories.SongRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;


@Component
public class DeezerApiConsumer {
    private static final String DEEZER_GENRE_URL = "https://api.deezer.com/genre";
    private final RestTemplate restTemplate;
    private final JSONParser parser;
    private final GenreRepository genreRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;
    private final GenreMapper genreMapper;
    private final ArtistMapper artistMapper;
    private  final AlbumMapper albumMapper;
    private final SongMapper songMapper;
    @Autowired
    public DeezerApiConsumer(RestTemplate restTemplate, JSONParser parser, GenreRepository genreRepository, AlbumRepository albumRepository, ArtistRepository artistRepository, SongRepository songRepository, GenreMapper genreMapper, ArtistMapper artistMapper, AlbumMapper albumMapper, SongMapper songMapper) {
        this.restTemplate = restTemplate;
        this.parser = parser;
        this.genreRepository = genreRepository;
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.songRepository = songRepository;
        this.genreMapper = genreMapper;
        this.artistMapper = artistMapper;
        this.albumMapper = albumMapper;
        this.songMapper = songMapper;
    }

    public void populateGenres() throws ParseException {
        String url = DEEZER_GENRE_URL;
        String response = restTemplate.getForObject(url, String.class);
        JSONObject object = (JSONObject) parser.parse(response);
        JSONArray array = (JSONArray) object.get("data");
        for (int i = 1; i < array.size(); i++) {
            JSONObject dataObject = (JSONObject) array.get(i);
            Genre genre = genreMapper.fromJsonToGenre(dataObject);
            genreRepository.save(genre);

        }
    }

    public void populateArtists() throws ParseException {
        int i = 259;
        List<Artist> artistList = new ArrayList<>();
        while (i <= 259) {
            String artistUrl = String.format("https://api.deezer.com/artist/%d", i);
            String response = restTemplate.getForObject(artistUrl, String.class);
            JSONObject object = (JSONObject) parser.parse(response);
            Artist artist = artistMapper.fromJsonToArtist(object);
            artistList.add(artist);
            i++;
        }
        artistRepository.saveAll(artistList);
    }

    public void populateAlbums() throws ParseException {
        List<Artist> artists = artistRepository.findAll();
        List<Album> albumList = new ArrayList<>();
        int i = 1;
        while (i < artists.size()) {
            String albumUrl = String.format("https://api.deezer.com/artist/%d/albums", i);
            String albumResponse = restTemplate.getForObject(albumUrl, String.class);
            JSONObject albumJson = (JSONObject) parser.parse(albumResponse);
            JSONArray albumArray = (JSONArray) albumJson.get("data");
            for (int j = 0; j < albumArray.size() / 2; j++) {
                JSONObject singleAlbum = (JSONObject) albumArray.get(j);
                Long genreId = (Long) singleAlbum.get("genre_id");
                // one of Snoop Dogg albums is with genre_id -1 ?! Blame Deezer not me ! :)
                if (genreId == -1) {
                    continue;
                }
                Genre genre = genreRepository.getReferenceById(genreId);
                Album album = albumMapper.fromJsonToAlbum(singleAlbum);
                album.setGenre(genre);
                albumList.add(album);
            }
            albumRepository.saveAll(albumList);
        }

    }
    public void populateSongs() throws ParseException {
        List<Album> albumList = albumRepository.findAll();
        List<Song> songsList = new ArrayList<>();
        for (Album album : albumList
        ) {
            String trackUrl = String.format("https://api.deezer.com/album/%d", album.getId());
            String trackResponse = restTemplate.getForObject(trackUrl, String.class);
            JSONObject trackJsonObject = (JSONObject) parser.parse(trackResponse);
            JSONObject tracks = (JSONObject) trackJsonObject.get("tracks");
            JSONArray tracksArray = (JSONArray) tracks.get("data");
            JSONObject artist = (JSONObject) trackJsonObject.get("artist");
            Long artistId = (Long) artist.get("id");

            for (int k = 0; k < tracksArray.size() / 2; k++) {
                JSONObject singleJsonTrack = (JSONObject) tracksArray.get(k);
                Song song = songMapper.fromJsonToSong(singleJsonTrack);
                song.setArtist(artistRepository.getReferenceById(artistId));
                song.setAlbum(album);
                songsList.add(song);
            }

        }
        songRepository.saveAll(songsList);
    }
}

