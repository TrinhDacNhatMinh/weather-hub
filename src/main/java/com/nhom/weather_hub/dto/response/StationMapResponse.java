package com.nhom.weather_hub.dto.response;

import java.math.BigDecimal;

public record StationMapResponse(
        Long stationId,
        String stationName,
        BigDecimal latitude,
        BigDecimal longitude,
        WeatherSnapshot weather
) {
    public record WeatherSnapshot(
            Float temperature,
            Float humidity,
            Float windSpeed,
            Float rainfall,
            Float dust
    ) {}
}
