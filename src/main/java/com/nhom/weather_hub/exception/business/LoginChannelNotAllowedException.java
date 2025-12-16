package com.nhom.weather_hub.exception.business;

import com.nhom.weather_hub.exception.base.BusinessException;
import org.springframework.http.HttpStatus;

public class LoginChannelNotAllowedException extends BusinessException {
    public LoginChannelNotAllowedException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
