package com.sggc.app.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserHasNoGamesException extends Exception {
    private String userId;
    public UserHasNoGamesException(String message) {
        super(message);
    }

    @Override
    public String getMessage(){
        return "User with Id "+ userId +" has no games associated with their account";
    }
}
