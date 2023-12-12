package com.example.ridepalapplication.services;

import com.example.ridepalapplication.dtos.GenreDto;
import com.example.ridepalapplication.dtos.LocationDto;
import com.example.ridepalapplication.dtos.PlaylistDto;
import com.example.ridepalapplication.exceptions.AuthorizationException;
import com.example.ridepalapplication.exceptions.EntityDuplicateException;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.helpers.AuthorizationHelper;
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
    AuthorizationHelper authorizationHelper;
    @Mock
    TagRepository tagMockRepository;
    @InjectMocks
    PlaylistServiceImpl service;


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


    // --- choosePlaylistStrategy ---
    @Test
    void choosePlaylistStrategy_ShouldReturnGenerateTopRankSongsUniqueArtistsPlaylistMethod_WhenTopRankAndUniqueArtist() {

        Playlist playlist = new Playlist();
        PlaylistDto playlistDto = createTopRankUniqueArtistsPlaylistDto();
        int travelDuration = 60;

        Mockito.verify(Mockito.when(service.choosePlaylistStrategy(playlistDto, playlist, travelDuration, playlistDto.getGenreDtoList())).thenReturn(playlist));

        //top rank + unique artist
        //we need to verify that the following method is called:
        //generateTopRankSongsUniqueArtistsPlaylist
    }

    @Test
    void choosePlaylistStrategy_ShouldReturnGenerateTopRankSongsNonUniqueArtistPlaylist_WhenTopRankNoUniqueArtist() {

        PlaylistDto playlistDto = createTopRankUniqueArtistsPlaylistDto();
        playlistDto.setUniqueArtists(false);

        //top rank + NO unique artist
        //we need to verify that the following method is called:
        //generateTopRankSongsNonUniqueArtistPlaylist
    }

    @Test
    void choosePlaylistStrategy_ShouldReturnGenerateTopRankSongsUniqueArtistsPlaylist_WhenNoTopRankUniqueArtist() {

        PlaylistDto playlistDto = createTopRankUniqueArtistsPlaylistDto();
        playlistDto.setTopRank(false);

        //no top rank + unique artist
        //we need to verify that the following method is called:
        //generateTopRankSongsUniqueArtistsPlaylist //TODO: ask Boby why this method
    }

    @Test
    void choosePlaylistStrategy_ShouldReturnGenerateDefaultRankNonUniqueArtistPlaylist_WhenNoTopRankNoUniqueArtist() {

        PlaylistDto playlistDto = createTopRankUniqueArtistsPlaylistDto();
        playlistDto.setUniqueArtists(false);
        playlistDto.setTopRank(false);

        //no top rank + no unique artist
        //we need to verify that the following method is called:
        //generateDefaultRankNonUniqueArtistPlaylist
    }





//    @Test
//    void generatePlaylist_Should_ReturnNewPlaylist() {
//        Playlist mockPlaylist = new Playlist();
//        int travelDuration = 100;
//
//        List<GenreDto> genresList = new ArrayList<>();
//
//        GenreDto rockGenreDto = new GenreDto("rock", 50);
//        genresList.add(rockGenreDto);
//
//        GenreDto popGenreDto = new GenreDto("pop", 50);
//        genresList.add(popGenreDto);
//
//        Genre mockRockGenre = createMockGenre();
//        mockRockGenre.setName("rock");
//        Mockito.when(genreMockRepository.findByName("rock")).thenReturn(mockRockGenre);
//
//        Genre mockPopGenre = createMockGenre();
//        mockPopGenre.setName("pop");
//        Mockito.when(genreMockRepository.findByName("pop")).thenReturn(mockPopGenre);
//
//        Song mockSong = createMockSong();
//        Mockito.when(songMockRepository.getMeSingleSongByGenre(anyLong())).thenReturn(List.of(mockSong));
//
//        Mockito.when(mockRepository.save(mockPlaylist)).thenReturn(mockPlaylist);
//
//        Playlist result = service.generateDefaultRankUniqueArtistsPlaylist(new Playlist(), travelDuration, genresList);
//
//        Mockito.verify(mockRepository, Mockito.times(1)).save(mockPlaylist);
//    }

    @Test
    void updateName_Should_ReturnUpdatedPlaylistName_When_SameUser() {
        User mockUser = createMockUser();
        Playlist mockPlaylist = createMockPlaylist();
        String newName = "new_playlist_name";

        service.update(mockUser, mockPlaylist, newName);

        Assertions.assertTrue(mockPlaylist.getName().equals(newName));

        Mockito.verify(mockRepository, Mockito.times(1))
                .save(mockPlaylist);
    }

    @Test
    void updateName_Should_ReturnUpdatedPlaylistName_When_Admin() {
        User mockAdminUser = createAdminMockUser();
        Playlist mockPlaylist = createMockPlaylist();
        String newName = "new_name";

        service.update(mockAdminUser, mockPlaylist, newName);

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

        Mockito.doThrow(AuthorizationException.class)
                .when(authorizationHelper)
                .checkAuthorization(otherMockUser, mockPlaylist.getCreator(), "update playlist name");

        Assertions.assertThrows(AuthorizationException.class,
                () -> service.update(otherMockUser, mockPlaylist, newName));
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
        User otherMockUser = create2ndMockUser();

        Playlist playlist = createMockPlaylist();
        Song song = createMockSong();

        Mockito.doThrow(AuthorizationException.class)
                .when(authorizationHelper)
                .checkAuthorization(otherMockUser, playlist.getCreator(), "update playlist songs");

        Assertions.assertThrows(AuthorizationException.class,
                () -> service.deleteSong(otherMockUser, song, playlist));
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
        User otherMockUser = create2ndMockUser();

        Playlist playlist = createMockPlaylist();
        Tag tag = createMockTag();

        Mockito.doThrow(AuthorizationException.class)
                .when(authorizationHelper)
                .checkAuthorization(otherMockUser, playlist.getCreator(), "add tags to other users' playlists");

        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.createTag(otherMockUser, tag, playlist));
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

        Assertions.assertEquals(String.format("Tag with name %s already exists.", tag.getName()), exception.getMessage());
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

        Mockito.doThrow(AuthorizationException.class)
                .when(authorizationHelper)
                .checkAuthorization(otherMockUser, playlist.getCreator(), "delete tags from other users' playlists");

        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.deleteTag(otherMockUser, tag, playlist));
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

        Assertions.assertEquals(String.format("Tag with name %s not found.", tag.getName()), exception.getMessage());
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
        User otherMockUser = create2ndMockUser();
        Playlist mockPlaylist = createMockPlaylist();

        Mockito.when(mockRepository.findById(mockPlaylist.getId())).thenReturn(Optional.of(mockPlaylist));

        Mockito.doThrow(AuthorizationException.class)
                .when(authorizationHelper)
                .checkAuthorization(otherMockUser, mockPlaylist.getCreator().getId(), "delete the playlist");

        Assertions.assertThrows(AuthorizationException.class,
                () -> service.delete(otherMockUser, mockPlaylist.getId()));
    }


}
