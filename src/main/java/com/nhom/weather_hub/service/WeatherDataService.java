package com.nhom.weather_hub.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nhom.weather_hub.dto.request.WeatherDataRequest;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.WeatherDataResponse;

import java.time.Instant;

public interface WeatherDataService {

    public void handleIncomingMqttData(String payload) throws JsonProcessingException;

    public WeatherDataResponse getWeatherDataById(Long id);

    public PageResponse<WeatherDataResponse> getWeatherData(Long stationId, Instant from, Instant to, int page, int size);

    public WeatherDataResponse getLatestWeatherData(Long stationId);

    public void deleteByStation(Long stationId);

}
