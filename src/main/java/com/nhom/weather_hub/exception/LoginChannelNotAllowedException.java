package com.nhom.weather_hub.exception;

public class LoginChannelNotAllowedException extends RuntimeException {
    public LoginChannelNotAllowedException(String message) {
        super(message);
    }
}
