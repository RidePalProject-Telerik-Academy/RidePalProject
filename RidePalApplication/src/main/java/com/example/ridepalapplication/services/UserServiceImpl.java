package com.example.ridepalapplication.services;

import com.example.ridepalapplication.exceptions.EntityDuplicateException;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.helpers.CheckPermissions;
import com.example.ridepalapplication.models.Role;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.repositories.RoleRepository;
import com.example.ridepalapplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.ridepalapplication.helpers.CheckPermissions.checkAuthorization;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
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
//        if (loadUserByUsername(user.getUsername()) == null) {
//            usernameExists = false;
//        }
//        if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
//            usernameExists = false;
//        }
        if (usernameExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }
        if (userRepository.findByEmail(user.getEmail()) == null) {
            emailExists = false;
        }
        if (emailExists) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        Role userRole = roleRepository.findByAuthority("USER").get();

        Set<Role> authorities = new HashSet<>();

        authorities.add(userRole);

        return userRepository.save(new User(
                user.getUsername(),
                encodedPassword,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                authorities));
    }

    @Override
    public User updateUser(User loggedUser, User userToBeUpdated) {
        checkAuthorization(loggedUser, userToBeUpdated, "update other users");

        boolean emailExists = true;

        if (userRepository.findByEmail(userToBeUpdated.getEmail()) == null) {
            emailExists = false;
        }
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
        CheckPermissions.checkAuthorization(loggedUser, id, "delete other users");

        Optional<User> userToUpdate = userRepository.findById(id);
        if (userToUpdate.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("User", id);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
