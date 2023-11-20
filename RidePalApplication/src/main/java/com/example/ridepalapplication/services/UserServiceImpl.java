package com.example.ridepalapplication.services;

import com.example.ridepalapplication.exceptions.AuthorizationException;
import com.example.ridepalapplication.exceptions.EntityDuplicateException;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.ridepalapplication.helpers.CheckPermissions.checkIfSameUser;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User", id);
        } else return user;
    }

    @Override
    public User getByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("User", "username", username);
        } else return user;
    }

    @Override
    public User createUser(User user) {
        boolean usernameExists = true;
        boolean emailExists = true;
        if (userRepository.findByUsername(user.getUsername()) == null) {
            usernameExists = false;
        }
        if (usernameExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }
        if (userRepository.findByEmail(user.getEmail()) == null) {
            emailExists = false;
        }
        if (emailExists) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User loggedUser, User userToBeUpdated) {
        checkIfSameUser(loggedUser, userToBeUpdated, "update");

        boolean emailExists = true;

        if (userRepository.findByEmail(userToBeUpdated.getEmail()) == null) {
            emailExists = false;
        }
        if (emailExists) {
            throw new EntityDuplicateException("User", "email", userToBeUpdated.getEmail());
        }

        return userRepository.save(userToBeUpdated);
    }

    @Override
    public void deleteUser(User loggedUser, Long id) {
        checkIfSameUser(loggedUser, id, "delete");
        userRepository.deleteById(id);
    }
}
