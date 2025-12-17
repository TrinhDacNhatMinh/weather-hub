package com.nhom.weather_hub.service;

import com.nhom.weather_hub.dto.request.ThresholdRequest;
import com.nhom.weather_hub.dto.response.ThresholdResponse;

public interface ThresholdService {

    void createDefaultThreshold(Long stationId);

    ThresholdResponse getByStationId(Long stationId);

    ThresholdResponse updateThreshold(Long id, ThresholdRequest request);

}
