package com.nhom.weather_hub.service;

import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.WeatherDataResponse;

import java.time.Instant;

public interface WeatherDataService {

    void handleIncomingMqttData(String payload);

    WeatherDataResponse getWeatherDataById(Long id);

    PageResponse<WeatherDataResponse> getWeatherData(Long stationId, Instant from, Instant to, int page, int size);

    WeatherDataResponse getLatestWeatherData(Long stationId);

    void deleteByStation(Long stationId);

}
