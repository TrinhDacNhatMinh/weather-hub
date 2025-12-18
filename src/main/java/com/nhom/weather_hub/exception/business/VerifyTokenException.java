package com.nhom.weather_hub.exception.business;

import com.nhom.weather_hub.exception.base.BusinessException;
import org.springframework.http.HttpStatus;

public class VerifyTokenException extends BusinessException {
    public VerifyTokenException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
