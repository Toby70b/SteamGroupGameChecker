package com.sggc.app.controller;


import com.sggc.app.errors.ApiError;
import com.sggc.app.exception.UserHasNoGamesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@RestControllerAdvice
public class SGGCControllerAdvice extends ResponseEntityExceptionHandler {

    @Value("${sggc.api.version}")
    private String currentApiVersion;
    private static Logger LOGGER = LoggerFactory.getLogger(SGGCControllerAdvice.class);
    @ExceptionHandler(UserHasNoGamesException.class)
    public ResponseEntity<ApiError> handleUserHasNoGames(UserHasNoGamesException ex) {

        final ApiError error = new ApiError(
                currentApiVersion,
                Integer.toString(HttpStatus.NOT_FOUND.value()),
                "User Has No Games",
                "UserHasNoGamesException",
                "User with Id: "+ex.getUserId()+" has no games associated with their account "
        );
        LOGGER.error("ERROR:", ex);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiError> handleIOException(IOException ex) {
        final ApiError error = new ApiError(
                currentApiVersion,
                Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                "Internal Server Error",
                "IOException",
                "Internal server error, error within code, please check the logs..."
        );
        LOGGER.error("ERROR:", ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}