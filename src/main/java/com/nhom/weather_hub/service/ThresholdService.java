package com.nhom.weather_hub.service;

import com.nhom.weather_hub.dto.request.ThresholdRequest;
import com.nhom.weather_hub.dto.response.ThresholdResponse;

public interface ThresholdService {

    public void createDefaultThreshold(Long stationId);

    public ThresholdResponse getByStationId(Long stationId);

    public ThresholdResponse updateThreshold(Long id, ThresholdRequest request);

}
