package com.example.ridepalapplication.helpers;

import com.example.ridepalapplication.exceptions.AuthorizationException;
import com.example.ridepalapplication.models.User;

public class CheckPermissions {
    public static final String UNAUTHORIZED_OPERATION = "You are not allowed to %s.";


    public static void checkAuthorization(User loggedUser, Long id, String operation) {
        if (!loggedUser.isAdmin() && loggedUser.getId() != id) {
            throw new AuthorizationException(String.format(UNAUTHORIZED_OPERATION, operation));
        }
    }

    public static void checkAuthorization(User loggedUser, User userToUpdate, String operation) {
        if (!loggedUser.isAdmin() && loggedUser.getId() != userToUpdate.getId()) {
            throw new AuthorizationException(String.format(UNAUTHORIZED_OPERATION, operation));
        }
    }


}
