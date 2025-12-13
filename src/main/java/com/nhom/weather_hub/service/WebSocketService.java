package com.nhom.weather_hub.service;

import com.nhom.weather_hub.dto.response.AlertResponse;
import com.nhom.weather_hub.dto.response.WeatherDataResponse;

public interface WebSocketService {

    public void sendWeatherData(Long stationId, WeatherDataResponse response);

    public void sendAlert(Long stationId, AlertResponse response);

}
