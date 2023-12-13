package com.example.ridepalapplication;

import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.dtos.LocationDto;
import com.example.ridepalapplication.dtos.PlaylistDto;
import com.example.ridepalapplication.models.*;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MockHelpers {

    public static User createMockUser() {
        Set<Role> authorities = new HashSet<>();
        authorities.add(new Role("USER"));
        User user = new User("mock_username",
                "mockFirstName",
                "mockFirstName",
                "mock_email@gmail.com",
                "mockPassword98!@",
                authorities);
        user.setId(1L);
        return user;
    }

    public static User create2ndMockUser() {
        Set<Role> authorities = new HashSet<>();
        authorities.add(new Role("USER"));
        User user = new User("mock_username_2",
                "mockFirstName_2",
                "mockFirstName2",
                "mock_email2@gmail.com",
                "mockPassword982!@",
                authorities);
        user.setId(2L);
        return user;
    }

    public static User createAdminMockUser() {
        Set<Role> authorities = new HashSet<>();
        authorities.add(new Role("ADMIN"));
        User user = new User("admin_mock_username",
                "AdminMockFirstName",
                "AdminMockFirstName",
                "admin_mock_user@user.com",
                "Admin_MockPassword98!@",
                authorities);
        user.setId(3L);
        return user;
    }

    public static Playlist createMockPlaylist() {
        var mockPlaylist = new Playlist();
        mockPlaylist.setId(1L);
        mockPlaylist.setName("mock_playlist");
        mockPlaylist.setDuration(Math.toIntExact(createMockSong().getDuration()));
        mockPlaylist.setRank(createMockSong().getRank());
        mockPlaylist.setCreator(createMockUser());
        mockPlaylist.addSong(createMockSong());
        mockPlaylist.setTags(createMockTag());
        return mockPlaylist;
    }

    public static Song createMockSong() {
        var mockSong = new Song();
        mockSong.setId(1L);
        mockSong.setTitle("Mock Tile");
        mockSong.setLink("https://www.deezer.com/track/538624");
        mockSong.setDuration(200L);
        mockSong.setRank(200000L);
        mockSong.setPreviewUrl("https://cdns-preview-6.dzcdn.net/stream/c-6b9ae26c7daf2c25ef97d89e3a7f4b0b-4.mp3");
        mockSong.setArtist(createMockArtist());
        mockSong.setAlbum(createMockAlbum());
        return mockSong;
    }

    public static Artist createMockArtist() {
        var mockArtist = new Artist();
        mockArtist.setId(1L);
        mockArtist.setName("Mock Artist");
        mockArtist.setArtistTrackListUrl("https://api.deezer.com/artist/1/top?limit=50");
        return mockArtist;
    }

    public static Album createMockAlbum() {
        var mockAlbum = new Album();
        mockAlbum.setId(1L);
        mockAlbum.setName("Mock Album");
        mockAlbum.setGenre(createMockGenre());
        mockAlbum.setAlbumTrackListUrl("https://api.deezer.com/album/71450/tracks");
        return mockAlbum;
    }

    public static Genre createMockGenre() {
        var mockGenre = new Genre();
        mockGenre.setId(1L);
        mockGenre.setName("Pop");
        return mockGenre;
    }

    public static Tag createMockTag() {
        var mockTag = new Tag();
        mockTag.setId(1L);
        mockTag.setName("Mock");
        return mockTag;
    }

    public static LocationDto createLocationDto() {
        LocationDto locationDto = new LocationDto();
        locationDto.setStartLocation("Sofia");
        locationDto.setStartLocation("Pernik");

        return locationDto;
    }

    public static List<GenreDto> createMockGenreDtoList(){
        GenreDto mockPop = new GenreDto("Pop",50);

        return List.of(mockPop);
    }
    public static Playlist createAlgorithmMockPlaylist(){
        Playlist playlist = new Playlist();
        User user = createMockUser();
        playlist.setCreator(user);
        playlist.setName("MockitoName");
        return playlist;
    }
    public static PlaylistDto createMockPlayListDto(){
        PlaylistDto playlistDto = new PlaylistDto();
        LocationDto locationDto = createLocationDto();
        List<GenreDto> genreDtoList = createMockGenreDtoList();
        playlistDto.setName("MockName");
        playlistDto.setLocationDto(locationDto);
        playlistDto.setGenreDtoList(genreDtoList);
        return playlistDto;
    }
}
