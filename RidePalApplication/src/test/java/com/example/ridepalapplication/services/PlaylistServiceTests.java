package com.example.ridepalapplication.services;

import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.exceptions.AuthorizationException;
import com.example.ridepalapplication.exceptions.EntityDuplicateException;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.models.*;
import com.example.ridepalapplication.repositories.GenreRepository;
import com.example.ridepalapplication.repositories.PlaylistRepository;
import com.example.ridepalapplication.repositories.SongRepository;
import com.example.ridepalapplication.repositories.TagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.ridepalapplication.MockHelpers.*;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class PlaylistServiceTests {

    @Mock
    PlaylistRepository mockRepository;
    @Mock
    GenreRepository genreMockRepository;
    @Mock
    SongRepository songMockRepository;
    @Mock
    TagRepository tagMockRepository;
    @InjectMocks
    PlaylistServiceImpl service;

//    @Test
//    void getAll_Should_ReturnAllPlaylists() {
//        Playlist mockPlaylist = new Playlist();
//        List<Playlist> allPlaylists = new ArrayList<>();
//        allPlaylists.add(mockPlaylist);
//
//        Mockito.when(mockRepository.findAll())
//                .thenReturn(allPlaylists);
//
//        List<Playlist> result = service.getAll();
//        Assertions.assertEquals(allPlaylists, result);
//
//        Mockito.verify(mockRepository, Mockito.times(1))
//                .findAll();
//    }

    @Test
    void getById_Should_ReturnPlaylist() {
        Playlist mockPlaylist = createMockPlaylist();

        Mockito.when(mockRepository.findById(mockPlaylist.getId()))
                .thenReturn(Optional.of(mockPlaylist));

        Playlist result = service.getById(mockPlaylist.getId()).orElse(null);
        Assertions.assertEquals(mockPlaylist, result);

        Mockito.verify(mockRepository, Mockito.times(1))
                .findById(anyLong());
    }

    @Test
    void getById_Should_ThrowException_When_IdNotFound() {

        long mockId = anyLong();

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> service.getById(mockId));

        Assertions.assertEquals(String.format("Playlist with id %s not found.", mockId), exception.getMessage());
    }

    @Test
    //TODO: fix this test logic
    void generatePlaylist_Should_ReturnNewPlaylist() {
        Playlist mockPlaylist = new Playlist();
        int travelDuration = 100;

        List<GenreDto> genresList = new ArrayList<>();

        GenreDto rockGenreDto = new GenreDto("rock", 50);
        genresList.add(rockGenreDto);

        GenreDto popGenreDto = new GenreDto("pop", 50);
        genresList.add(popGenreDto);

        Genre mockRockGenre = createMockGenre();
        mockRockGenre.setName("rock");
        Mockito.when(genreMockRepository.findByName("rock")).thenReturn(mockRockGenre);

        Genre mockPopGenre = createMockGenre();
        mockPopGenre.setName("pop");
        Mockito.when(genreMockRepository.findByName("pop")).thenReturn(mockPopGenre);

        Song mockSong = createMockSong();
        Mockito.when(songMockRepository.getMeSingleSongByGenre(anyLong())).thenReturn(List.of(mockSong));

        Mockito.when(mockRepository.save(mockPlaylist)).thenReturn(mockPlaylist);

        Playlist result = service.generatePlaylist(new Playlist(), travelDuration, genresList);

        Mockito.verify(mockRepository, Mockito.times(1)).save(mockPlaylist);
    }

    @Test
    void updateName_Should_ReturnUpdatedPlaylistName_When_SameUser() {
        User mockUser = createMockUser();
        Playlist mockPlaylist = createMockPlaylist();
        String newName = "new_name";

        service.updateName(mockUser, mockPlaylist, newName);

        Assertions.assertTrue(mockPlaylist.getName().equals(newName));

        Mockito.verify(mockRepository, Mockito.times(1))
                .save(mockPlaylist);
    }

    @Test
    void updateName_Should_ReturnUpdatedPlaylistName_When_Admin() {
        User mockAdminUser = createAdminMockUser();
        Playlist mockPlaylist = createMockPlaylist();
        String newName = "new_name";

        service.updateName(mockAdminUser, mockPlaylist, newName);

        Assertions.assertTrue(mockPlaylist.getName().equals(newName));

        Mockito.verify(mockRepository, Mockito.times(1))
                .save(mockPlaylist);
    }

    @Test
    void updateName_Should_ThrowException_When_OtherUser() {
        User otherMockUser = createMockUser();
        otherMockUser.setId(2);
        otherMockUser.setUsername("other_user");

        Playlist mockPlaylist = createMockPlaylist();
        String newName = "new_name";

        AuthorizationException exception = Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.updateName(otherMockUser, mockPlaylist, newName));

        Assertions.assertEquals(String.format("You are not allowed to update playlist."), exception.getMessage());
    }

    @Test
    void addSong_Should_ReturnPlaylistWithAddedSong_When_Song_NotExist_AndSameUser() {
        User mockUser = createMockUser();
        Playlist playlist = createMockPlaylist();
        Song song = createMockSong();
        song.setId(2);

        service.addSong(mockUser, song, playlist);

        Assertions.assertTrue(playlist.getSongs().contains(song));

        Mockito.verify(mockRepository, Mockito.times(1))
                .save(playlist);
    }

    @Test
    void addSong_Should_ReturnPlaylistWithAddedSong_When_Song_NotExist_AndAdmin() {
        User mockAdminUser = createAdminMockUser();
        Playlist playlist = createMockPlaylist();
        Song song = createMockSong();
        song.setId(2);

        service.addSong(mockAdminUser, song, playlist);

        Assertions.assertTrue(playlist.getSongs().contains(song));

        Mockito.verify(mockRepository, Mockito.times(1))
                .save(playlist);
    }

    @Test
    void addSong_Should_ThrowException_When_Song_Exist() {
        User mockAdminUser = createAdminMockUser();
        Playlist playlist = createMockPlaylist();
        Song song = createMockSong();

        EntityDuplicateException exception = Assertions.assertThrows(
                EntityDuplicateException.class,
                () -> service.addSong(mockAdminUser, song, playlist));

        Assertions.assertEquals(String.format("Song with title %s already exists in the playlist.", song.getTitle()), exception.getMessage());
    }

    @Test
    void deleteSong_Should_ReturnPlaylistWithDeletedSong_When_Song_Exist_AndSameUser() {
        User mockUser = createMockUser();
        Playlist playlist = createMockPlaylist();
        Song song = createMockSong();
        //create a 2nd song in the playlist to avoid the 0 pointer exception
        song.setId(2);
        song.setTitle("New Song");
        playlist.addSong(song);

        //TODO: here, if I don't make a 2nd song and add it (if I am trying to delete the only 1 song in my playlist),
        // I hit a corner case with Arithmetic / by zero exception when calculating the rank in the Service
        service.deleteSong(mockUser, song, playlist);

        Assertions.assertFalse(playlist.getSongs().contains(song));

        Mockito.verify(mockRepository, Mockito.times(1))
                .save(playlist);
    }

    @Test
    void deleteSong_Should_ReturnPlaylistWithDeletedSong_When_Song_Exist_AndAdmin() {
        User mockAdminUser = createAdminMockUser();
        Playlist playlist = createMockPlaylist();
        Song song = createMockSong();
        song.setId(2);
        song.setTitle("New Song");
        playlist.addSong(song);

        service.deleteSong(mockAdminUser, song, playlist);

        Assertions.assertFalse(playlist.getSongs().contains(song));

        Mockito.verify(mockRepository, Mockito.times(1))
                .save(playlist);
    }

    @Test
    void deleteSong_Should_ThrowException_When_OtherUser() {
        User otherMockUser = createMockUser();
        otherMockUser.setId(2);
        otherMockUser.setUsername("other_user");

        Playlist playlist = createMockPlaylist();
        Song song = createMockSong();

        AuthorizationException exception = Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.deleteSong(otherMockUser, song, playlist));

        Assertions.assertEquals(String.format("You are not allowed to update playlist songs."), exception.getMessage());
    }

    @Test
    void deleteSong_Should_ThrowException_When_NotFound() {
        User mockUser = createMockUser();
        Playlist playlist = createMockPlaylist();
        Song otherSong = createMockSong();
        otherSong.setId(2L);

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> service.deleteSong(mockUser, otherSong, playlist));

        Assertions.assertEquals(String.format("Song with title %s not found.", otherSong.getTitle()), exception.getMessage());
    }

    @Test
    void createTag_Should_AddTagToPlaylist_When_Tag_NotExist_AndSameUser() {
        User mockUser = createMockUser();
        Playlist playlist = createMockPlaylist();
        Tag tag = createMockTag();

        Mockito.when(tagMockRepository.findByName(tag.getName()))
                .thenReturn(null);

        service.createTag(mockUser, tag, playlist);

        Mockito.verify(mockRepository, Mockito.times(1))
                .save(playlist);
    }

    @Test
    void createTag_Should_AddTagToPlaylist_When_Tag_NotExist_AndSameAdmin() {
        User mockAdminUser = createAdminMockUser();
        Playlist playlist = createMockPlaylist();
        Tag tag = createMockTag();

        Mockito.when(tagMockRepository.findByName(tag.getName()))
                .thenReturn(null);

        service.createTag(mockAdminUser, tag, playlist);

        Assertions.assertTrue(playlist.getTags().contains(tag));

        Mockito.verify(mockRepository, Mockito.times(1))
                .save(playlist);
    }

    @Test
    void createTag_Should_ThrowException_When_OtherUser() {
        User otherMockUser = createMockUser();
        otherMockUser.setId(2);
        otherMockUser.setUsername("other_user");

        Playlist playlist = createMockPlaylist();
        Tag tag = createMockTag();

        AuthorizationException exception = Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.createTag(otherMockUser, tag, playlist));

        Assertions.assertEquals(String.format("You are not allowed to add tags to other users' playlists."), exception.getMessage());
    }

    @Test
    void createTag_Should_ThrowException_When_Tag_Exist() {
        User mockUser = createMockUser();
        Playlist playlist = createMockPlaylist();
        Tag tag = createMockTag();

        Mockito.when(tagMockRepository.findByName(tag.getName()))
                .thenReturn(tag);

        EntityDuplicateException exception = Assertions.assertThrows(
                EntityDuplicateException.class,
                () -> service.createTag(mockUser, tag, playlist));

        Assertions.assertEquals(String.format("Tag with playlist %s already exists.", tag.getName()), exception.getMessage());
    }

    @Test
    void deleteTag_Should_RemoveTagFromPlaylist_When_TagExist_And_SameUser() {
        User mockUser = createMockUser();
        Playlist playlist = createMockPlaylist();
        Tag tag = createMockTag();

        Mockito.when(tagMockRepository.findByName(tag.getName()))
                .thenReturn(tag);

        service.deleteTag(mockUser, tag, playlist);

        Assertions.assertFalse(playlist.getTags().contains(tag));

        Mockito.verify(mockRepository, Mockito.times(1))
                .save(playlist);
    }

    @Test
    void deleteTag_Should_RemoveTagFromPlaylist_When_TagExist_And_Admin() {
        User mockAdminUser = createAdminMockUser();
        Playlist playlist = createMockPlaylist();
        Tag tag = createMockTag();

        Mockito.when(tagMockRepository.findByName(tag.getName()))
                .thenReturn(tag);

        service.deleteTag(mockAdminUser, tag, playlist);

        Assertions.assertFalse(playlist.getTags().contains(tag));

        Mockito.verify(mockRepository, Mockito.times(1))
                .save(playlist);
    }

    @Test
    void deleteTag_Should_ThrowException_When_OtherUser() {
        User otherMockUser = createMockUser();
        otherMockUser.setId(2);
        otherMockUser.setUsername("other_user");
        Playlist playlist = createMockPlaylist();
        Tag tag = createMockTag();

        AuthorizationException exception = Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.deleteTag(otherMockUser, tag, playlist));

        Assertions.assertEquals(String.format("You are not allowed to delete tags from other users' playlists."), exception.getMessage());
    }

    @Test
    void deleteTag_Should_ThrowException_When_TagNotExist() {
        User mockUser = createMockUser();
        Playlist playlist = createMockPlaylist();
        Tag tag = createMockTag();

        Mockito.when(tagMockRepository.findByName(tag.getName()))
                .thenReturn(null);

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> service.deleteTag(mockUser, tag, playlist));

        Assertions.assertEquals(String.format("Tag with playlist %s not found.", tag.getName()), exception.getMessage());
    }


    @Test
    void delete_Should_DeletePlaylist_When_SameUser() {
        User mockUser = createMockUser();
        Playlist mockPlaylist = createMockPlaylist();

        Mockito.when(mockRepository.findById(mockPlaylist.getId())).thenReturn(Optional.of(mockPlaylist));

        service.delete(mockUser, mockPlaylist.getId());

        Mockito.verify(mockRepository, Mockito.times(1)).deleteById(mockPlaylist.getId());
    }

    @Test
    void delete_Should_DeletePlaylist_When_Admin() {
        User mockAdminUser = createAdminMockUser();
        Playlist mockPlaylist = createMockPlaylist();

        Mockito.when(mockRepository.findById(mockPlaylist.getId())).thenReturn(Optional.of(mockPlaylist));

        service.delete(mockAdminUser, mockPlaylist.getId());

        Mockito.verify(mockRepository, Mockito.times(1)).deleteById(mockPlaylist.getId());
    }

    @Test
    void delete_Should_ThrowException_When_OtherUser() {
        User mockUser = createMockUser();
        mockUser.setId(2);
        mockUser.setUsername("other_mock_user");
        Playlist mockPlaylist = createMockPlaylist();

        Mockito.when(mockRepository.findById(mockPlaylist.getId())).thenReturn(Optional.of(mockPlaylist));

        AuthorizationException exception = Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.delete(mockUser, mockPlaylist.getId()));

        Assertions.assertEquals(String.format("You are not allowed to delete the playlist."), exception.getMessage());
    }
}
