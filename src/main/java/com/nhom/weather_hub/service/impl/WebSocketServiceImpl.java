package com.nhom.weather_hub.service.impl;

import com.nhom.weather_hub.dto.response.AlertResponse;
import com.nhom.weather_hub.dto.response.WeatherDataResponse;
import com.nhom.weather_hub.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendAlert(Long stationId, AlertResponse alertResponse) {
        String destination = "/topic/alerts/" + stationId;
        messagingTemplate.convertAndSend(destination, alertResponse);
    }

    @Override
    public void sendWeatherData(Long stationId, WeatherDataResponse weatherDataResponse) {
        String destination = "/topic/weather" + stationId;
        messagingTemplate.convertAndSend(destination, weatherDataResponse);
    }

}
