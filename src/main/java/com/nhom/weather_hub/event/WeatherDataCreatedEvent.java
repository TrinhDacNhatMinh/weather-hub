package com.nhom.weather_hub.event;

import com.nhom.weather_hub.dto.response.WeatherDataResponse;

public record WeatherDataCreatedEvent(
        Long stationId,
        WeatherDataResponse weatherDataResponse
) {
}
