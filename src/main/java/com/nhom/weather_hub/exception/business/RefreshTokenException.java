package com.nhom.weather_hub.exception.business;

import com.nhom.weather_hub.exception.base.BusinessException;
import org.springframework.http.HttpStatus;

public class RefreshTokenException extends BusinessException {
    public RefreshTokenException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
