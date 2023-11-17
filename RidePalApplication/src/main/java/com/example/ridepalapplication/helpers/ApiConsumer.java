package com.example.ridepalapplication.helpers;


import com.example.ridepalapplication.mappers.GenreMapper;
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
public class ApiConsumer {
    private static final String DEEZER_GENRE_URL = "https://api.deezer.com/genre";
    private final RestTemplate restTemplate;
    private final JSONParser parser;
    private final GenreRepository genreRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;
    private final GenreMapper genreMapper;
    @Autowired
    public ApiConsumer(RestTemplate restTemplate, JSONParser parser, GenreRepository genreRepository, AlbumRepository albumRepository, ArtistRepository artistRepository, SongRepository songRepository, GenreMapper genreMapper) {
        this.restTemplate = restTemplate;
        this.parser = parser;
        this.genreRepository = genreRepository;
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.songRepository = songRepository;
        this.genreMapper = genreMapper;
    }

    public void getGenres() throws ParseException {
        String url = DEEZER_GENRE_URL;
        String response = restTemplate.getForObject(url, String.class);
        JSONObject object = (JSONObject) parser.parse(response);
        JSONArray array = (JSONArray) object.get("data");
        for (int i = 1; i < array.size(); i++) {
            JSONObject dataObject = (JSONObject) array.get(i);
            Genre genre = genreMapper.fromMapToGenre(dataObject);
            genreRepository.save(genre);

        }
    }
    public void getArtist() throws ParseException {
        int i = 1;
        List<Album> albumList = new ArrayList<>();
        List<Song> songList = new ArrayList<>();
        List<Artist> artistList = new ArrayList<>();
        while(i < 10) {
            // ------ CONSUME AND SAVE ARTISTS ------ //
            String artistUrl = String.format("https://api.deezer.com/artist/%d", i);
            String response = restTemplate.getForObject(artistUrl, String.class);
            JSONObject object = (JSONObject) parser.parse(response);
            Long id = (Long) object.get("id");
            String name = (String) object.get("name");
            String trackList = (String) object.get("tracklist");
            Artist artist = new Artist(); // artist
            artist.setId(id);
            artist.setName(name);
            artist.setArtistTrackListUrl(trackList);
            artistList.add(artist);

            // -------------------------------- //


            // ------ CONSUME AND SAVE ALBUMS ------ //

            String albumUrl = String.format("https://api.deezer.com/artist/%d/albums",i);
            String albumResponse = restTemplate.getForObject(albumUrl,String.class);
            JSONObject albumJson = (JSONObject) parser.parse(albumResponse);
            JSONArray albumArray = (JSONArray) albumJson.get("data");
            for (int j = 0; j <albumArray.size()/2 ; j++) {
                JSONObject singleAlbum = (JSONObject) albumArray.get(j);
               Long albumId = (Long) singleAlbum.get("id");
              String albumName = (String) singleAlbum.get("title");
              Long genreId = (Long) singleAlbum.get("genre_id");
              // one of Snoop Dogg albums is with genre_id -1 ?! Blame Deezer not me ! :)
              if(genreId == -1){
                  continue;
              }
              String albumTracklist = (String) singleAlbum.get("tracklist");
              Genre genre = genreRepository.getReferenceById(genreId);
                Album album = new Album();
                album.setId(albumId);
                album.setName(albumName);
                album.setAlbumTrackListUrl(albumTracklist);
                album.setGenre(genre);
                albumList.add(album);

                // -------------------------------- //


                // ------ CONSUME AND SAVE ALBUM TRACKS ------ //
                String trackUrl = String.format("https://api.deezer.com/album/%d",album.getId());
                String trackResponse = restTemplate.getForObject(trackUrl, String.class);
                JSONObject trackJsonObject = (JSONObject) parser.parse(trackResponse);
                JSONObject tracks = (JSONObject) trackJsonObject.get("tracks");
                Long artistId = (long) i;


                JSONArray tracksArray = (JSONArray) tracks.get("data");
                for (int k = 0; k <tracksArray.size()/2 ; k++) {
                    JSONObject singleJsonTrack = (JSONObject) tracksArray.get(k);
                   Long trackId = (Long) singleJsonTrack.get("id");
                  String trackTitle = (String) singleJsonTrack.get("title");
                   Long duration = (Long) singleJsonTrack.get("duration");
                   Long rank = (Long) singleJsonTrack.get("rank");
                  String link = (String) singleJsonTrack.get("link");
                  String preview = (String) singleJsonTrack.get("preview");
                    Song song = new Song();
                    song.setId(trackId);
                    song.setTitle(trackTitle);
                    song.setArtist(artistRepository.getReferenceById(artistId));
                    song.setAlbum(album);
                    song.setRank(rank);
                    song.setDuration(duration);
                    song.setLink(link);
                    song.setPreviewUrl(preview);
                    songList.add(song);
                    // -------------------------------- //
                }
            }

            i++;
            }

        albumRepository.saveAll(albumList);
        songRepository.saveAll(songList);
        artistRepository.saveAll(artistList);
        }
    }



