package com.sggc.app.errors;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class ErrorBlock {

    private String code;
    private String message;
    private List<Error> errors;

    public ErrorBlock(final String code, final String message, final String domain,
                      final String reason, final String errorMessage) {
        this.code = code;
        this.message = message;
        this.errors = Arrays.asList(new Error(domain, reason, errorMessage));
    }

    private ErrorBlock(final String code, final String message, final List<Error> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    public static ErrorBlock copyWithMessage(final ErrorBlock s, final String message) {
        return new ErrorBlock(s.code, message, s.getErrors());
    }
}
