package com.example.ridepalapplication.services;

import com.example.ridepalapplication.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();
    Optional<User> getById(Long id);
    User createUser(User user);
    User updateUser(User loggedUser,User userToBeUpdated);
    void deleteUser(User loggedUser , Long id);
}
