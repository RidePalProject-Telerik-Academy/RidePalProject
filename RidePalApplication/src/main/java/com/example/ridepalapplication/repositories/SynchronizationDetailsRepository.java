package com.example.ridepalapplication.repositories;

import com.example.ridepalapplication.models.SynchronizationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SynchronizationDetailsRepository extends JpaRepository<SynchronizationDetails, Long> {
}
