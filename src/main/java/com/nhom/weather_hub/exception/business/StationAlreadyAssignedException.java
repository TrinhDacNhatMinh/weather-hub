package com.nhom.weather_hub.exception.business;

import com.nhom.weather_hub.exception.base.BusinessException;
import org.springframework.http.HttpStatus;

public class StationAlreadyAssignedException extends BusinessException {
    public StationAlreadyAssignedException() {
        super(HttpStatus.CONFLICT, "Station already assigned to another user");
    }
}
