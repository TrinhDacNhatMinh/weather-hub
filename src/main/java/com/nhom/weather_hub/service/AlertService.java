package com.nhom.weather_hub.service;

import com.nhom.weather_hub.domain.records.ThresholdEvaluation;
import com.nhom.weather_hub.dto.response.AlertResponse;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.entity.Alert;
import com.nhom.weather_hub.entity.WeatherData;

import java.util.Optional;

public interface AlertService {

    Optional<ThresholdEvaluation> evaluateThresholds(WeatherData data);

    Alert createAlert(WeatherData data, ThresholdEvaluation thresholdEvaluation);

    AlertResponse getById(Long id);

    PageResponse<AlertResponse> getByUser(int page, int size);

    AlertResponse updateStatus(Long id, Alert.Status status);

    void deleteAlert(Long id);

    void deleteAllByUser(Long userId);

}
