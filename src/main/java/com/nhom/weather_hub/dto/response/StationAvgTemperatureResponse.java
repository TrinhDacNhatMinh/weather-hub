package com.nhom.weather_hub.dto.response;

import java.math.BigDecimal;

public record StationAvgTemperatureResponse(
        Long id,
        String name,
        BigDecimal latitude,
        BigDecimal longitude,
        Float avgTemperature
) {
}
