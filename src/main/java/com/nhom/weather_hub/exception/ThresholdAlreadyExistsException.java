package com.nhom.weather_hub.exception;

public class ThresholdAlreadyExistsException extends BusinessException {
    public ThresholdAlreadyExistsException(Long stationId) {
        super("Threshold already exists for station id " + stationId);
    }
}
