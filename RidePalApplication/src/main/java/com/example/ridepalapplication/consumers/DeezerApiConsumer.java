package com.example.ridepalapplication.consumers;


import com.example.ridepalapplication.exceptions.EntityNotFoundException;
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
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;


@Component
public class DeezerApiConsumer {
    private static final String DEEZER_GENRE_URL = "https://api.deezer.com/genre";
    private static final String DEEZER_ALBUM_URL = "https://api.deezer.com/artist/%d";
    private static final String DEEZER_ARTISTS_ALBUMS_URL = "https://api.deezer.com/artist/%d/albums";
    private static final String DEEZER_ALBUM_SONGS_URL = "https://api.deezer.com/album/%d";

    private final  WebClient webClient;
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
    public DeezerApiConsumer(WebClient webClient, JSONParser parser, GenreRepository genreRepository, AlbumRepository albumRepository, ArtistRepository artistRepository, SongRepository songRepository, GenreMapper genreMapper, ArtistMapper artistMapper, AlbumMapper albumMapper, SongMapper songMapper) {

        this.webClient = webClient;
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
        List <Genre> genres = new ArrayList<>();
        String response = webClient.get().uri(DEEZER_GENRE_URL)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        JSONObject object = (JSONObject) parser.parse(response);
        JSONArray array = (JSONArray) object.get("data");
        for (int i = 1; i < array.size(); i++) {
            JSONObject dataObject = (JSONObject) array.get(i);
            Genre genre = genreMapper.fromJsonToGenre(dataObject);
            genres.add(genre);
        }
        genreRepository.saveAll(genres);
    }

    public void populateArtists() throws ParseException {
        int i = 1;
        List<Artist> artistList = new ArrayList<>();
        while (i <= 1024) {
            String artistUrl = String.format(DEEZER_ALBUM_URL, i);
            String response = webClient.get().uri(artistUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            JSONObject object = (JSONObject) parser.parse(response);
            if(object.containsKey("error")){
                i++;
                continue;

            }
            Artist artist = artistMapper.fromJsonToArtist(object);
            artistList.add(artist);
            i++;
        }
        artistRepository.saveAll(artistList);
    }

    public void populateAlbums() throws ParseException {
        List<Artist> artists = artistRepository.findAll();
        List<Album> albumList = new ArrayList<>();
        for (Artist artist : artists) {

            String albumUrl = String.format(DEEZER_ARTISTS_ALBUMS_URL, artist.getId());
            String albumResponse = webClient.get().uri(albumUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            JSONObject albumJson = (JSONObject) parser.parse(albumResponse);
            JSONArray albumArray = (JSONArray) albumJson.get("data");
            for (int j = 0; j < albumArray.size() / 8; j++) {
                JSONObject singleAlbum = (JSONObject) albumArray.get(j);
                Long genreId = (Long) singleAlbum.get("genre_id");
                Genre genre;

                try {
                    genre = (genreRepository.findById(genreId).orElseThrow(
                           () -> new EntityNotFoundException("Genre", genreId)));
               }
               catch (EntityNotFoundException e){
                   continue;
               }


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
            String trackUrl = String.format(DEEZER_ALBUM_SONGS_URL, album.getId());
            String trackResponse = webClient.get().uri(trackUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            JSONObject trackJsonObject = (JSONObject) parser.parse(trackResponse);
            if(trackJsonObject.get("tracks")==null){
                continue;
            }
            JSONObject tracks = (JSONObject) trackJsonObject.get("tracks");
            JSONArray tracksArray = (JSONArray) tracks.get("data");
            JSONObject artist = (JSONObject) trackJsonObject.get("artist");
            Long artistId = (Long) artist.get("id");
            Artist existingArtist;
            try {
                existingArtist = artistRepository.findById(artistId).orElseThrow(
                        ()->new EntityNotFoundException("Artist",artistId));
            }
            catch (EntityNotFoundException e){
                existingArtist = artistMapper.fromJsonToArtist(artist);
                artistRepository.save(existingArtist);
            }

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

