package com.example.ridepalapplication.mappers;

import com.example.ridepalapplication.dtos.UpdateUserDto;
import com.example.ridepalapplication.dtos.UserDto;
import com.example.ridepalapplication.models.Role;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.repositories.RoleRepository;
import com.example.ridepalapplication.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class UserMapper {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserMapper(UserService userService, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User fromDto(long id, UpdateUserDto updateUserDto) {
        User existingUser = userService.getById(id).orElseThrow(EntityNotFoundException::new);

        existingUser.setFirstName(updateUserDto.getFirstName());
        existingUser.setLastName(updateUserDto.getLastName());
        existingUser.setEmail(updateUserDto.getEmail());
        existingUser.setPassword(updateUserDto.getPassword());

        return existingUser;
    }


    public User fromDto(UserDto userDto) {

        String password = passwordEncoder.encode(userDto.getPassword());
        Role userRole = roleRepository.findByAuthority("USER").get();

        Set<Role> rolesSet = new HashSet<>();
        rolesSet.add(userRole);

        return new User(
                userDto.getUsername(),
                password,
                userDto.getEmail(),
                userDto.getFirstName(),
                userDto.getLastName(),
                rolesSet
        );

    }

}
