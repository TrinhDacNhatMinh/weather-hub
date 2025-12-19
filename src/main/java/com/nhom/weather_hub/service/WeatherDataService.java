package com.nhom.weather_hub.service;

import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.WeatherDataResponse;

import java.time.Instant;

public interface WeatherDataService {

    void handleIncomingMqttData(String payload);

    WeatherDataResponse getWeatherData(Long id);

    void deleteWeatherDataByStation(Long stationId);

}
