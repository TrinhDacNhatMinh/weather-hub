package com.nhom.weather_hub.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String findWith, Object o) {
        super(resource + " with " + findWith + " " + o + " not found");
    }
}
