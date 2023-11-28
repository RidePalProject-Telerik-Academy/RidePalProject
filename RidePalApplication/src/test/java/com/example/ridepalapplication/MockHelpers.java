package com.example.ridepalapplication;

import com.example.ridepalapplication.models.*;

import java.util.HashSet;
import java.util.Set;

public class MockHelpers {

    public static User createMockUser() {
        Set<Role> authorities = new HashSet<>();
        authorities.add(new Role("USER"));
        return new User("mock_username",
                "mockFirstName",
                "mockFirstName",
                "mock_email@gmail.com",
                "mockPassword98!@",
                authorities);
    }

    public static User createAdminMockUser() {
        Set<Role> authorities = new HashSet<>();
        authorities.add(new Role("ADMIN"));
        return new User("admin_mock_username",
                "AdminMockFirstName",
                "AdminMockFirstName",
                "admin_mock_user@user.com",
                "Admin_MockPassword98!@",
                authorities);


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
        mockGenre.setName("Mock");
        return mockGenre;
    }

    public static Tag createMockTag() {
        var mockTag = new Tag();
        mockTag.setId(1L);
        mockTag.setName("Mock");
        return mockTag;
    }

}
