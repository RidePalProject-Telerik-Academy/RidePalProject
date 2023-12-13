package com.example.ridepalapplication.services;

import com.example.ridepalapplication.dtos.LocationDto;
import com.example.ridepalapplication.dtos.PlaylistDto;
import com.example.ridepalapplication.exceptions.AuthorizationException;
import com.example.ridepalapplication.exceptions.EntityDuplicateException;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.helpers.AuthorizationHelper;
import com.example.ridepalapplication.helpers.PlaylistHelper;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.ridepalapplication.MockHelpers.*;

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
    public  void getById_Should_ReturnPlaylist() {
        Playlist mockPlaylist = createMockPlaylist();

        Mockito.when(mockRepository.findById(mockPlaylist.getId()))
                .thenReturn(Optional.of(mockPlaylist));

         Optional<Playlist> result = service.getById(mockPlaylist.getId());

        Assertions.assertEquals(result,Optional.of(mockPlaylist));
    }

    @Test
    public void getById_Should_ThrowException_When_Id_NotFound() {
        Playlist playlist = createMockPlaylist();

       Mockito.when(mockRepository.findById(playlist.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,()->service.getById(playlist.getId()));
    }
    @Test
    public void getBy_CreatorId_Should_Call_Repository(){
        User user = createMockUser();

        service.getUserPlaylists(user.getId());

        Mockito.verify(mockRepository,Mockito.times(1)).findPlaylistByCreatorId(user.getId());
    }

    @Test
    public void getMost_Recent_Should_Return_Nine_Playlists(){

        List<Playlist> mockList = new ArrayList<>(Collections.nCopies(9, new Playlist()));

        Mockito.when(mockRepository.getMostRecent()).thenReturn(mockList);

        List<Playlist> actualList = service.getMostRecent();

        Assertions.assertEquals(9, actualList.size());
    }
    @Test
    public void getAll_Should_Call_Repository(){
        service.getAll();

        Mockito.verify(mockRepository,Mockito.times(1)).findAll();
    }

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

    @Test
    void choose_Playlist_Strategy_Should_Return_Default_Function_When_NonArgs_Specified(){
        Playlist playlist = createMockPlaylist();
        PlaylistDto playlistDto = createMockPlayListDto();

        Song song = createMockSong();
        Genre genre = createMockGenre();
        int timeDuration = 16160;

        Mockito.when(genreMockRepository.findByName(Mockito.anyString())).thenReturn(genre);
        Mockito.when(songMockRepository.getMeSingleSongByGenre(Mockito.anyLong())).thenReturn(Collections.singletonList(song));

        service.choosePlaylistStrategy(playlistDto,playlist,timeDuration);

        Mockito.verify(songMockRepository,Mockito.atLeast(1)).getMeSingleSongByGenre(Mockito.anyLong());

    }
    @Test
    void choose_Playlist_Strategy_Should_Return_Non_UniqueArtists_Top_Rank_Function_When_Valid_Args(){
        Playlist playlist = createMockPlaylist();
        PlaylistDto playlistDto = createMockPlayListDto();
        playlistDto.setTopRank(true);

        Song song = createMockSong();
        Genre genre = createMockGenre();
        int timeDuration = 16160;

        Mockito.when(genreMockRepository.findByName(Mockito.anyString())).thenReturn(genre);
        Mockito.when(songMockRepository.getMeSingleTopSongByGenre(Mockito.anyLong())).thenReturn(Collections.singletonList(song));
        service.choosePlaylistStrategy(playlistDto,playlist,timeDuration);

        Mockito.verify(songMockRepository,Mockito.atLeast(1)).getMeSingleTopSongByGenre(Mockito.anyLong());

    }
    @Test
    void choose_Playlist_Strategy_Should_Return_UniqueArtists_Top_Rank_Function_When_Valid_Args(){
        Playlist playlist = createMockPlaylist();
        PlaylistDto playlistDto = createMockPlayListDto();
        playlistDto.setTopRank(true);
        playlistDto.setUniqueArtists(true);



        Song song = createMockSong();
        Genre genre = createMockGenre();
        List<Song> songList = new ArrayList<>();
        songList.add(song);
        List<Long> longList = new ArrayList<>();
        longList.add(song.getArtist().getId());
        int timeDuration = 500;

        Mockito.when(genreMockRepository.findByName(Mockito.anyString())).thenReturn(genre);

        Mockito.when(songMockRepository.getMeSingleTopSongByGenre(song.getId())).thenReturn(songList);

        Mockito.when(songMockRepository.getMeSingleTopSongByGenreAndUniqueArtist(genre.getId(), longList)).thenReturn(songList);

        service.choosePlaylistStrategy(playlistDto,playlist,timeDuration);

        Mockito.verify(songMockRepository,Mockito.atLeast(1)).getMeSingleTopSongByGenreAndUniqueArtist(Mockito.anyLong(),Mockito.anyList());

    }

    @Test
    public void choose_Strategy_Should_Return_generate_Default_Rank_Unique_Artists_Playlist_When_Valid_Args(){
        Playlist playlist = createMockPlaylist();
        PlaylistDto playlistDto = createMockPlayListDto();
        playlistDto.setUniqueArtists(true);


        Song song = createMockSong();
        Genre genre = createMockGenre();
        List<Song> songList = new ArrayList<>();
        songList.add(song);
        List<Long> longList = new ArrayList<>();
        longList.add(song.getArtist().getId());
        int timeDuration = 500;

        Mockito.when(genreMockRepository.findByName(Mockito.anyString())).thenReturn(genre);

        Mockito.when(songMockRepository.getMeSingleSongByGenre(genre.getId())).thenReturn(songList);

        Mockito.when(songMockRepository.getMeSingleSongByGenreAndUniqueArtist(genre.getId(),longList)).thenReturn(songList);

        service.choosePlaylistStrategy(playlistDto,playlist,timeDuration);

        Mockito.verify(songMockRepository,Mockito.atLeast(1)).getMeSingleSongByGenreAndUniqueArtist(Mockito.anyLong(),Mockito.anyList());

    }
}
