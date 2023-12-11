package com.example.ridepalapplication.services;

import com.example.ridepalapplication.exceptions.AuthorizationException;
import com.example.ridepalapplication.exceptions.EntityDuplicateException;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.helpers.AuthorizationHelper;
import com.example.ridepalapplication.models.Playlist;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.repositories.PlaylistRepository;
import com.example.ridepalapplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthorizationHelper authorizationHelper;
    private final PlaylistRepository playlistRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthorizationHelper authorizationHelper, PlaylistRepository playlistRepository) {
        this.userRepository = userRepository;
        this.authorizationHelper = authorizationHelper;

        this.playlistRepository = playlistRepository;
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
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User", "username", username));
    }

    @Override
    public User createUser(User user) {
        boolean usernameExists = true;
        boolean emailExists = true;
        try {
            loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException e) {
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
        authorizationHelper.checkAuthorization(loggedUser, userToBeUpdated, "update other users");

        boolean emailExists = userRepository.findByEmail(userToBeUpdated.getEmail()) != null;

        if (emailExists) {
            User userWithSameEmail = userRepository.findByEmail(userToBeUpdated.getEmail());
            if (!userWithSameEmail.equals(userToBeUpdated)) {
                throw new EntityDuplicateException("User", "email", userToBeUpdated.getEmail());
            }
        }

        return userRepository.save(userToBeUpdated);
    }

    @Override
    public void deleteUser(User loggedUser, Long id) {
        authorizationHelper.checkAuthorization(loggedUser, id, "delete other users");
        Optional<User> userToUpdate = userRepository.findById(id);
        List<Playlist> userPlaylists = playlistRepository.findAllByCreator(userToUpdate.orElseThrow(()->new EntityNotFoundException("User",id)));
        playlistRepository.deleteAll(userPlaylists);
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
