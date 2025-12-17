package com.nhom.weather_hub.domain.records;

import java.time.Instant;

public record WeatherDataRequest(
        String apiKey,
        Float temperature,
        Float humidity,
        Float windSpeed,
        Float rainfall,
        Float dust,
        Instant recordAt
) {
}
