package com.nhom.weather_hub.event;

import com.nhom.weather_hub.dto.response.WeatherDataResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WeatherDataCreatedEvent {
    private final Long stationId;
    private final WeatherDataResponse weatherDataResponse;
}
