package com.example.ridepalapplication.repositories;

import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long>, JpaSpecificationExecutor<Playlist> {
    List<Playlist> findPlaylistByNameContainingAndDurationBetweenAndTagsInOrderByRankDesc(String name, Integer min, Integer max, Collection<Tag> tag);

    List<Playlist> findPlaylistByNameContainingAndDurationBetweenOrderByRankDesc(String name, Integer min, Integer max);
}
