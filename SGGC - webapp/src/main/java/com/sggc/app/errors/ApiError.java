package com.sggc.app.errors;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class ApiError {

    private final String apiVersion;
    private ErrorBlock error;

    public ApiError(String apiVersion, String code, String message, String reason, String errorMessage) {
        this.apiVersion = apiVersion;
        this.error = new ErrorBlock(code, message, reason, errorMessage);
    }

    public static ApiError fromDefaultAttributeMap(String apiVersion,
                                                   Map<String, Object> defaultErrorAttributes) {
        return new ApiError(
                apiVersion,
                ((Integer) defaultErrorAttributes.get("status")).toString(),
                (String) defaultErrorAttributes.getOrDefault("message", "no message available"),
                (String) defaultErrorAttributes.getOrDefault("error", "no reason available"),
                (String) defaultErrorAttributes.get("message")
        );
    }

    // utility method to return a map of serialized root attributes,
    // see the last part of the guide for more details
    public Map<String, Object> toAttributeMap() {
        Map<String, Object> apiVersion = new HashMap<>();
        apiVersion.put("apiVersion", this.apiVersion);
        apiVersion.put("error", getError());
        return apiVersion;
    }
}