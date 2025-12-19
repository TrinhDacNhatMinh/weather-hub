package com.nhom.weather_hub.service;

import com.nhom.weather_hub.dto.request.UpdateThresholdRequest;
import com.nhom.weather_hub.dto.response.ThresholdResponse;

public interface ThresholdService {

    void initializeDefaultThreshold(Long stationId);

    ThresholdResponse getThresholdByStationId(Long stationId);

    ThresholdResponse updateThreshold(Long id, UpdateThresholdRequest request);

}
