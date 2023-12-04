package com.example.ridepalapplication.helpers;
import com.example.ridepalapplication.exceptions.AuthorizationException;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationHelper {

    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(Authentication authentication){
        String username;
        try {
            username = authentication.getName();
        }
        catch (NullPointerException e){
            throw new AuthorizationException("Invalid Authorization");
        }
       return userService.getByUsername(username);
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }

        return authentication.isAuthenticated();
    }

}

