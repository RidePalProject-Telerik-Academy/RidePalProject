package com.example.ridepalapplication.repositories;

import com.example.ridepalapplication.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre,Long> {
    Genre findByName(String name);
}
