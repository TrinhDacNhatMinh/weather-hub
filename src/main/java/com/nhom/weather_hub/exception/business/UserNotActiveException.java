package com.nhom.weather_hub.exception.business;

import com.nhom.weather_hub.exception.base.BusinessException;
import org.springframework.http.HttpStatus;

public class UserNotActiveException extends BusinessException {
    public UserNotActiveException() {
        super(HttpStatus.FORBIDDEN, "User is not active");
    }
}
