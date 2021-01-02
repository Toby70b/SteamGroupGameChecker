package com.sggc.exceptions;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserHasNoGamesException extends Exception {
    private String userId;
    public UserHasNoGamesException() {
        super();
    }
}
