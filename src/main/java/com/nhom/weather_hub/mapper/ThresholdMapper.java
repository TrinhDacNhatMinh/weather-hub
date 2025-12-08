package com.nhom.weather_hub.mapper;

import com.nhom.weather_hub.dto.request.ThresholdRequest;
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

    public void updateEntity(ThresholdRequest request, Threshold entity) {
        if (request == null) {
            return;
        }

        entity.setTemperatureMin(request.getTemperatureMin());
        entity.setTemperatureMax(request.getTemperatureMax());
        entity.setHumidityMin(request.getHumidityMin());
        entity.setHumidityMax(request.getHumidityMax());
        entity.setRainfallMax(request.getRainfallMax());
        entity.setWindSpeedMax(request.getWindSpeedMax());
        entity.setDustMax(request.getDustMax());
        entity.setTemperatureActive(request.getTemperatureActive());
        entity.setHumidityActive(request.getHumidityActive());
        entity.setRainfallActive(request.getRainfallActive());
        entity.setWindSpeedActive(request.getWindSpeedActive());
        entity.setDustActive(request.getDustActive());
    }

}
