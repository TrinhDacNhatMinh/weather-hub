package com.nhom.weather_hub.service;

import com.nhom.weather_hub.dto.response.DailyWeatherSummaryResponse;
import com.nhom.weather_hub.dto.response.WeatherDataResponse;

import java.util.List;

public interface WeatherDataService {

    void handleIncomingMqttData(String payload);

    WeatherDataResponse getWeatherData(Long id);

    List<DailyWeatherSummaryResponse> getDailySummary(Long stationId, int day);

    void deleteWeatherDataByStation(Long stationId);

}
