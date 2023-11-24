package com.example.ridepalapplication.exceptions;

public class EntityDuplicateException extends RuntimeException{
    public EntityDuplicateException(String type, String attribute, String value) {
        super(String.format("%s with %s %s already exists.", type, attribute, value));
    }

    public EntityDuplicateException(String type, String attribute, String value, String message) {
        super(String.format("%s with %s %s already exists %s.", type, attribute, value, message));
    }

}
