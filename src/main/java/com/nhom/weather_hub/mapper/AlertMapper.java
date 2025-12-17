package com.nhom.weather_hub.mapper;

import com.nhom.weather_hub.dto.response.AlertResponse;
import com.nhom.weather_hub.entity.Alert;
import org.springframework.stereotype.Component;

@Component
public class AlertMapper {

    public AlertResponse toResponse(Alert entity) {
        if (entity == null) {
            return null;
        }

        Long weatherDataId = null;
        Long stationId = null;
        String stationName = null;

        if (entity.getWeatherData() != null) {
            weatherDataId = entity.getWeatherData().getId();
            if (entity.getWeatherData().getStation() != null) {
                stationId = entity.getWeatherData().getStation().getId();
                stationName = entity.getWeatherData().getStation().getName();
            }
        }

        return new AlertResponse(
                entity.getId(),
                entity.getMessage(),
                entity.getStatus().toString(),
                entity.getCreatedAt(),
                weatherDataId,
                stationId,
                stationName
        );
    }

}
