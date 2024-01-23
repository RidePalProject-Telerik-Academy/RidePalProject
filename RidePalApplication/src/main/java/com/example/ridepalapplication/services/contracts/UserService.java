package com.example.ridepalapplication.services.contracts;

import com.example.ridepalapplication.dtos.UserDto;
import com.example.ridepalapplication.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    List<User> getAll();
    Optional<User> getById(Long id);
    User getByUsername(String username);
    User createUser(User user);
    User updateUser(User loggedUser,User userToBeUpdated);
    void deleteUser(User loggedUser , Long id);
}
