package com.example.ridepalapplication.helpers;

import com.example.ridepalapplication.exceptions.AuthorizationException;
import com.example.ridepalapplication.exceptions.EntityNotFoundException;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

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


    public User tryGetUser(HttpHeaders headers){
        String authorization = Objects.requireNonNull(headers.get("Authorization")).get(0);
        String base64Credentials = authorization.substring("Basic".length()).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        final String[] values = credentials.split(":", 2);
        final String username = values[0];
       return userService.getByUsername(username);
    }
}

