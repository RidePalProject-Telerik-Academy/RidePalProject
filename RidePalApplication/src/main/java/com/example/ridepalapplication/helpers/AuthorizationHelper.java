package com.example.ridepalapplication.helpers;

import com.example.ridepalapplication.exceptions.AuthorizationException;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.repositories.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationHelper {

    private final RoleRepository roleRepository;

    public AuthorizationHelper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    private  final String UNAUTHORIZED_OPERATION = "You are not allowed to %s.";


    public  void checkAuthorization(User loggedUser, Long id, String operation) {
        if (!loggedUser.getAuthorities().contains(roleRepository.findByAuthority("ADMIN")) && loggedUser.getId() != id) {
            throw new AuthorizationException(String.format(UNAUTHORIZED_OPERATION, operation));
        }
    }

    public  void checkAuthorization(User loggedUser, User userToUpdate, String operation) {
        if (loggedUser.getId() != userToUpdate.getId()) {
            throw new AuthorizationException(String.format(UNAUTHORIZED_OPERATION, operation));
        }
    }
}
