package com.example.ridepalapplication.repositories;

import com.example.ridepalapplication.models.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface SongRepository extends JpaRepository<Song,Long> {
    List<Song> findByAlbum_GenreName(String name);
    @Query(nativeQuery = true,value = "SELECT songs.* FROM songs JOIN albums ON songs.album_id = albums.id WHERE albums.genre_id = :id ORDER BY RAND() LIMIT 1;")
    List<Song> getMeSingleSongByGenre(@Param("id") Long id);
}
