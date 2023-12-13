package com.example.ridepalapplication.services;

import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.models.Album;
import com.example.ridepalapplication.repositories.AlbumRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.ridepalapplication.MockHelpers.createMockAlbum;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceTests {
    @Mock
    AlbumRepository albumRepository;
    @InjectMocks
    AlbumServiceImpl albumService;


    @Test
    public void testFindAll() {
        // Arrange
        int page = 1;
        int pageSize = 10;
        String albumTitle = "Some Title";
        String genre = "Some Genre";

        PageRequest pageable = PageRequest.of(page, pageSize);

        List<Album> mockAlbums = Collections.singletonList(new Album());
        Page<Album> mockAlbumPage = new PageImpl<>(mockAlbums, pageable, 1);
        Mockito.when(albumRepository.findAll(Mockito.any(Specification.class), Mockito.eq(pageable)))
                .thenReturn(mockAlbumPage);

        List<Album> result = albumService.findAll(page, pageSize, albumTitle, genre);

        Assertions.assertEquals(mockAlbums, result);
    }
    @Test
    public void getBy_Id_ShouldReturn_If_Album_Exists(){
        Album mockAlbum = createMockAlbum();

        Mockito.when(albumRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockAlbum));
        Optional<Album> result = albumService.getById(mockAlbum.getId());

        Assertions.assertEquals(result, Optional.of(mockAlbum));

    }
    @Test
    public void getBy_Should_Throw_If_Album_Not_Exists(){
        Album mockAlbum = createMockAlbum();

        Mockito.when(albumRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,()->albumService.getById(mockAlbum.getId()));
    }

    @Test
    public void testFindAllEmptyResult() {
        // Arrange
        int page = 1;
        int pageSize = 10;
        String albumTitle = "mockTitle";
        String genre = "mockGenre";

        PageRequest pageable = PageRequest.of(page, pageSize);

        Page<Album> mockEmptyAlbumPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        Mockito.when(albumRepository.findAll(Mockito.any(Specification.class), Mockito.eq(pageable)))
                .thenReturn(mockEmptyAlbumPage);

        // Act
        List<Album> result = albumService.findAll(page, pageSize, albumTitle, genre);

        // Assert
        Assertions.assertTrue(result.isEmpty());
    }
}
