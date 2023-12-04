package com.example.ridepalapplication.repositories;

import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.Tag;
import com.example.ridepalapplication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long>, JpaSpecificationExecutor<Playlist> {
    List<Playlist> findAllByCreator(User user);

    @Query(nativeQuery = true, value = "SELECT * FROM playlists ORDER BY id DESC LIMIT 10")
    List<Playlist> getMostRecent();

}
