package com.example.ridepalapplication.repositories;

import com.example.ridepalapplication.models.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album,Long>, JpaSpecificationExecutor<Album> {
}
