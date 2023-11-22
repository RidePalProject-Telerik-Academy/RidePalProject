package com.example.ridepalapplication.repositories;

import com.example.ridepalapplication.models.Tag;
import com.example.ridepalapplication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String tagName);
}
