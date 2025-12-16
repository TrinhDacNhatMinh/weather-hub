package com.nhom.weather_hub.exception.business;

import com.nhom.weather_hub.exception.base.BusinessException;
import org.springframework.http.HttpStatus;

public class ThresholdAlreadyExistsException extends BusinessException {
    public ThresholdAlreadyExistsException(Long stationId) {
        super(HttpStatus.CONFLICT, "Threshold already exists for station id " + stationId);
    }
}
