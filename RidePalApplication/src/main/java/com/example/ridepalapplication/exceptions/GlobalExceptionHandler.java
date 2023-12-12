package com.example.ridepalapplication.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.nio.file.AccessDeniedException;
import java.nio.file.NotDirectoryException;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT expired: " + e.getMessage());
    }
    @ExceptionHandler(DecodingException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleDecodingException(DecodingException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Decoding error" +e.getMessage());
    }

    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleMalformedJwtException(MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("JWT malformed: " + e.getMessage());
    }
    @ExceptionHandler(AccountStatusException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleAccountStatusException(AccountStatusException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User account is abnormal: " + e.getMessage());
    }
    @ExceptionHandler(InvalidBearerTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleInvalidBearerTokenException(InvalidBearerTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The access token provided is expired , malformed or invalid: " + e.getMessage());
    }
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No permission: " + e.getMessage());
    }
    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleSignatureException(SignatureException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token: " + e.getMessage());
    }


    }






