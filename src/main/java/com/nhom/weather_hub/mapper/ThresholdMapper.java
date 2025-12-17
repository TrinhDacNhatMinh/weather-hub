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

        ThresholdResponse thresholdResponse = new ThresholdResponse();
        thresholdResponse.setId(entity.getId());
        thresholdResponse.setTemperatureMin(entity.getTemperatureMin());
        thresholdResponse.setTemperatureMax(entity.getTemperatureMax());
        thresholdResponse.setHumidityMin(entity.getHumidityMin());
        thresholdResponse.setHumidityMax(entity.getHumidityMax());
        thresholdResponse.setRainfallMax(entity.getRainfallMax());
        thresholdResponse.setWindSpeedMax(entity.getWindSpeedMax());
        thresholdResponse.setDustMax(entity.getDustMax());
        thresholdResponse.setTemperatureActive(entity.getTemperatureActive());
        thresholdResponse.setHumidityActive(entity.getHumidityActive());
        thresholdResponse.setRainfallActive(entity.getRainfallActive());
        thresholdResponse.setWindSpeedActive(entity.getWindSpeedActive());
        thresholdResponse.setDustActive(entity.getDustActive());
        if (entity.getStation() != null) {
            thresholdResponse.setStationId(entity.getStation().getId());
        }

        return thresholdResponse;
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
