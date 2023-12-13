package com.example.ridepalapplication.repositories;

import com.example.ridepalapplication.models.SynchronizationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SyncGenresRepository extends JpaRepository<SynchronizationDetails, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM synchronizations ORDER BY id DESC LIMIT 10")
    List<SynchronizationDetails> getMostRecent();
}
