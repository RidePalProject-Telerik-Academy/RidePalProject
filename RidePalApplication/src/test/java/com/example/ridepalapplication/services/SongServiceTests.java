package com.example.ridepalapplication.services;

import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.models.Song;
import com.example.ridepalapplication.repositories.SongRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static com.example.ridepalapplication.MockHelpers.createMockSong;
import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
public class SongServiceTests {

    @Mock
    SongRepository songRepository;
    @InjectMocks
    SongServiceImpl songService;


    @Test
    public void findAll_Should_Call_Repository(){
        songService.findAll();

        Mockito.verify(songRepository,Mockito.times(1)).findAll();
    }
    @Test
    public void findBy_Id_Should_Return_When_Existing_Id(){
        Song mockSong = createMockSong();

        Mockito.when(songRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(mockSong));
        Optional<Song> result = songService.getById(mockSong.getId());

        Assertions.assertEquals(result,Optional.of(mockSong));
    }
    @Test
    public void findBy_Id_Should_Throw_When_NonExisting_Id(){
        Song mockSong = createMockSong();

        Mockito.when(songRepository.findById(mockSong.getId())).thenReturn(Optional.empty());


        Assertions.assertThrows(EntityNotFoundException.class,()->songService.getById(mockSong.getId()));
    }
    @Test
    public void get_By_Title_And_Artist_Should_Return_When_Valid_Arguments() {
        // Arrange
        String songTitle = "TestTitle";
        String artistName = "TestArtist";

        Song mockSong = new Song();
        mockSong.setTitle(songTitle);

        Mockito.when(songRepository.findByTitleAndArtistName(songTitle, artistName))
                .thenReturn(Collections.singletonList(mockSong));


        Song result = songService.getByTitleAndArtist(songTitle, artistName);

        assertEquals(mockSong, result);
    }

    @Test
    public void get_By_Title_And_Artist_Should_Throw_When_NonValid_Arguments() {
        // Arrange
        String songTitle = "mockTitle";
        String artistName = "mockArtist";



        Mockito.when(songRepository.findByTitleAndArtistName(songTitle, artistName))
                .thenReturn(Collections.emptyList());

        Assertions.assertThrows(EntityNotFoundException.class,()-> songService.getByTitleAndArtist(songTitle,artistName));
    }
}
