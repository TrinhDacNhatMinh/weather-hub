package com.nhom.weather_hub.service;

import com.nhom.weather_hub.dto.response.CurrentWeatherDataResponse;
import com.nhom.weather_hub.dto.response.DailyWeatherSummaryResponse;
import com.nhom.weather_hub.dto.response.HourWeatherDataSummaryResponse;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.StationAvgTemperatureResponse;
import com.nhom.weather_hub.dto.response.WeatherDataResponse;

import java.util.List;

public interface WeatherDataService {

    void handleIncomingMqttData(String payload);

    WeatherDataResponse getWeatherData(Long id);

    List<DailyWeatherSummaryResponse> getDailySummary(Long stationId, int day);

    List<HourWeatherDataSummaryResponse> getHourSummary(Long stationId, int hour);

    List<StationAvgTemperatureResponse> getAvgTemperature();

    PageResponse<CurrentWeatherDataResponse> getCurrentWeatherDataForUserAndPublicStations(int page, int size);

    void deleteWeatherDataByStation(Long stationId);

}
