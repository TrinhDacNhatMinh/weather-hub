package com.nhom.weather_hub.exception.business;

import com.nhom.weather_hub.exception.base.BusinessException;
import org.springframework.http.HttpStatus;

public class AccountNotActiveException extends BusinessException {
    public AccountNotActiveException() {
        super(HttpStatus.FORBIDDEN, "User is not active");
    }
}
