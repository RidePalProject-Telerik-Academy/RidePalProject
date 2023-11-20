package com.example.ridepalapplication.mappers;

import com.example.ridepalapplication.dtos.UpdateUserDto;
import com.example.ridepalapplication.dtos.UserDto;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserMapper {
    private final UserService userService;

    @Autowired
    public UserMapper(UserService userService) {
        this.userService = userService;
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
        User user = new User();

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());

        return user;
    }

}
