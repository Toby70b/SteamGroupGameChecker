package com.sggc.app.controller;


import com.sggc.app.errors.ApiError;
import com.sggc.app.exception.UserHasNoGamesException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class SGGCControllerAdvice extends ResponseEntityExceptionHandler {

    @Value("${requests.api.version}")
    private String currentApiVersion;

    @ExceptionHandler(UserHasNoGamesException.class)
    public ResponseEntity<ApiError> handleNonExistingRequest(UserHasNoGamesException ex) {
        final ApiError error = new ApiError(
                currentApiVersion,
                Integer.toString(HttpStatus.NOT_FOUND.value()),
                "User Has No Games",
                "UserHasNoGamesException",
                "User with Id: "+ex.getUserId()+" has no games associated with their account "
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}