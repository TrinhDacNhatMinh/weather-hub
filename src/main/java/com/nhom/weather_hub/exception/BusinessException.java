package com.nhom.weather_hub.exception;

public class BusinessException extends RuntimeException {
    protected BusinessException(String message) {
        super(message);
    }
}
