package com.nhom.weather_hub.exception.business;

import com.nhom.weather_hub.exception.base.BusinessException;
import org.springframework.http.HttpStatus;

public class PermissionDeniedException extends BusinessException {
    public PermissionDeniedException() {
        super(HttpStatus.FORBIDDEN, "Permission denied");
    }
}
