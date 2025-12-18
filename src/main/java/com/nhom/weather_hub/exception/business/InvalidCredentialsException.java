package com.nhom.weather_hub.exception.business;

import com.nhom.weather_hub.exception.base.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException() {
        super(HttpStatus.UNAUTHORIZED, "Invalid username or password");
    }
}
