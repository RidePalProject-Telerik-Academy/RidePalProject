package com.example.ridepalapplication.helpers;


import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Component
public class AuthenticationHelper {

    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(Authentication authentication){
        String username = authentication.getName();
       return userService.getByUsername(username);
    }
}

