package com.nhom.weather_hub.listener;

import com.nhom.weather_hub.event.WeatherDataCreatedEvent;
import com.nhom.weather_hub.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherDataListener {

    private final WebSocketService webSocketService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleWeatherDataCreated(WeatherDataCreatedEvent event) {
        try {
            webSocketService.sendWeatherData(event.stationId(), event.weatherDataResponse());
        } catch (Exception e) {
            log.error("Failed to send weather data via WebSocket", e);
        }
    }

}
