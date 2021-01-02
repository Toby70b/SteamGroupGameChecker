    package com.sggc.controllers;


import com.sggc.errors.ApiError;
import com.sggc.errors.Error;
import com.sggc.exceptions.UserHasNoGamesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class SGGCControllerAdvice extends ResponseEntityExceptionHandler {

    @Value("${sggc.api.version}")
    private String currentApiVersion;
    private Logger logger = LoggerFactory.getLogger(SGGCControllerAdvice.class);
    @ExceptionHandler(UserHasNoGamesException.class)
    public ResponseEntity<ApiError> handleUserHasNoGames(UserHasNoGamesException ex) {

        final ApiError error = new ApiError(
                currentApiVersion,
                Integer.toString(HttpStatus.NOT_FOUND.value()),
                "User Has No Games",
                "UserHasNoGamesException",
                "User with Id: "+ex.getUserId()+" has no games associated with their account, or doesn't exist "
        );
        logger.error("ERROR:", ex);
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
        logger.error("ERROR:", ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final ApiError apiError = new ApiError(
                currentApiVersion,
                Integer.toString(BAD_REQUEST.value()),
                "Argument value not valid",
                "MethodArgumentNotValidException",
                "Argument value not valid"
        );
        List<Error> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {

            errors.add(  new Error("MethodArgumentNotValidException", error.getRejectedValue() + " " + error.getDefaultMessage()));
        }
        apiError.getError().setErrors(errors);
        logger.error("ERROR:", ex);
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }
}