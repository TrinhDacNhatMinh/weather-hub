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

        AlertResponse alertResponse = new AlertResponse();
        alertResponse.setId(entity.getId());
        alertResponse.setMessage(entity.getMessage());
        alertResponse.setStatus(entity.getStatus().toString());
        alertResponse.setCreatedAt(entity.getCreatedAt());
        if (entity.getWeatherData() != null) {
            alertResponse.setWeatherDataId(entity.getWeatherData().getId());
            if (entity.getWeatherData().getStation() != null) {
                alertResponse.setStationId(entity.getWeatherData().getStation().getId());
                alertResponse.setStationName(entity.getWeatherData().getStation().getName());
            }
        }

        return alertResponse;
    }

}
