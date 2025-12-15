package com.nhom.weather_hub.service;

import com.nhom.weather_hub.domain.records.ThresholdEvaluation;
import com.nhom.weather_hub.dto.response.AlertResponse;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.entity.Alert;
import com.nhom.weather_hub.entity.WeatherData;

import java.util.Optional;

public interface AlertService {

    public Optional<ThresholdEvaluation> evaluateThresholds(WeatherData data);

    public Alert createAlert(WeatherData data, ThresholdEvaluation thresholdEvaluation);

    public AlertResponse getById(Long id);

    public PageResponse<AlertResponse> getByUser(int page, int size);

    public AlertResponse updateStatus(Long id, Alert.Status status);

    public void deleteAlert(Long id);

    public void deleteAllByUser(Long userId);

}
