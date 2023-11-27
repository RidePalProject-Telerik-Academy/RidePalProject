package com.example.ridepalapplication.repositories;

import com.example.ridepalapplication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
//    User findByUsername(String username);
//    // TODO: 26-Nov-23 счупих 2 теста :(

    User findByEmail(String email);

}
