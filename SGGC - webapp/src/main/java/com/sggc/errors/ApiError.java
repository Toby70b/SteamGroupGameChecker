package com.sggc.errors;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Data
public class ApiError {

    private final String apiVersion;
    private final String code;
    private final String exception;
    private final String errorMessage;


    public static ApiError fromDefaultAttributeMap(String apiVersion,
                                                   Map<String, Object> defaultErrorAttributes) {
        return new ApiError(
                apiVersion,
                ((Integer) defaultErrorAttributes.get("status")).toString(),
                (String) defaultErrorAttributes.getOrDefault("exception", "no message available"),
                (String) defaultErrorAttributes.get("message")
        );
    }

    public Map<String, Object> toAttributeMap() {
        Map<String, Object> apiVersion = new HashMap<>();
        apiVersion.put("apiVersion", this.apiVersion);
        apiVersion.put("code", this.apiVersion);
        apiVersion.put("exception", this.apiVersion);
        apiVersion.put("errorMessage", this.apiVersion);
        return apiVersion;
    }
}