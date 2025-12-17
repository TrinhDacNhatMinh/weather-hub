package com.nhom.weather_hub.mapper;

import com.nhom.weather_hub.dto.request.UpdateThresholdRequest;
import com.nhom.weather_hub.dto.response.ThresholdResponse;
import com.nhom.weather_hub.entity.Threshold;
import org.springframework.stereotype.Component;

@Component
public class ThresholdMapper {

    public ThresholdResponse toResponse(Threshold entity) {
        if (entity == null) {
            return null;
        }

        Long stationId = null;
        if (entity.getStation() != null) {
            stationId = entity.getStation().getId();
        }

        return new ThresholdResponse(
                entity.getId(),
                entity.getTemperatureMin(),
                entity.getTemperatureMax(),
                entity.getHumidityMin(),
                entity.getHumidityMax(),
                entity.getRainfallMax(),
                entity.getWindSpeedMax(),
                entity.getDustMax(),
                entity.getTemperatureActive(),
                entity.getHumidityActive(),
                entity.getRainfallActive(),
                entity.getWindSpeedActive(),
                entity.getDustActive(),
                stationId
        );
    }

    public void updateEntity(UpdateThresholdRequest request, Threshold entity) {
        if (request == null) {
            return;
        }

        entity.setTemperatureMin(request.temperatureMin());
        entity.setTemperatureMax(request.temperatureMax());
        entity.setHumidityMin(request.humidityMin());
        entity.setHumidityMax(request.humidityMax());
        entity.setRainfallMax(request.rainfallMax());
        entity.setWindSpeedMax(request.windSpeedMax());
        entity.setDustMax(request.dustMax());
        entity.setTemperatureActive(request.temperatureActive());
        entity.setHumidityActive(request.humidityActive());
        entity.setRainfallActive(request.rainfallActive());
        entity.setWindSpeedActive(request.windSpeedActive());
        entity.setDustActive(request.dustActive());
    }

}
