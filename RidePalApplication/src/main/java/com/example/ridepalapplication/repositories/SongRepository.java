package com.example.ridepalapplication.repositories;

import com.example.ridepalapplication.models.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song,Long> , JpaSpecificationExecutor<Song> {

    @Query(nativeQuery = true,value = "SELECT songs.* FROM songs JOIN albums ON songs.album_id = albums.id WHERE albums.genre_id = :id ORDER BY RAND() LIMIT 1;")
    List<Song> getMeSingleSongByGenre(@Param("id") Long id);
    @Query(nativeQuery = true,value = "SELECT songs.* FROM songs JOIN albums ON songs.album_id = albums.id WHERE albums.genre_id = :id ORDER BY songs.`rank`DESC ,RAND() LIMIT 1;")
    List<Song> getMeSingleTopSongByGenre(@Param("id") Long id);

    @Query(nativeQuery = true,value = "SELECT songs.* FROM songs JOIN albums ON songs.album_id = albums.id WHERE albums.genre_id = :id and songs.artist_id not in :artistId ORDER BY RAND() LIMIT 1;")
    List<Song> getMeSingleSongByGenreAndUniqueArtist(@Param("id") Long id, @Param("artistId") List<Long> artistId);
    @Query(nativeQuery = true,value = "SELECT songs.* FROM songs JOIN albums ON songs.album_id = albums.id WHERE albums.genre_id = :id and songs.artist_id not in :artistId ORDER BY songs.`rank` DESC ,RAND() LIMIT 1;")
    List<Song> getMeSingleTopSongByGenreAndUniqueArtist(@Param("id") Long id, @Param("artistId") List<Long> artistId);

    List<Song> findByTitleAndArtistName(String name, String artist);
}
