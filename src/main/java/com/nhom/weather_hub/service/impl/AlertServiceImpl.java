package com.nhom.weather_hub.service.impl;

import com.nhom.weather_hub.dto.response.AlertResponse;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.entity.Alert;
import com.nhom.weather_hub.entity.Station;
import com.nhom.weather_hub.entity.Threshold;
import com.nhom.weather_hub.entity.WeatherData;
import com.nhom.weather_hub.exception.ResourceNotFoundException;
import com.nhom.weather_hub.mapper.AlertMapper;
import com.nhom.weather_hub.repository.AlertRepository;
import com.nhom.weather_hub.repository.ThresholdRepository;
import com.nhom.weather_hub.service.AlertService;
import com.nhom.weather_hub.service.UserService;
import com.nhom.weather_hub.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;
    private final ThresholdRepository thresholdRepository;
    private final AlertMapper alertMapper;
    private final WebSocketService webSocketService;
    private final UserService userService;

    @Override
    @Transactional
    public AlertResponse checkAndCreateAlert(WeatherData data) {
        Threshold threshold = thresholdRepository.findByStationId(data.getStation().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Station", "id", data.getStation().getId()));

        int temperatureStatus = checkThreshold(
                threshold.getTemperatureActive(),
                data.getTemperature(),
                threshold.getTemperatureMin(),
                threshold.getTemperatureMax()
        );

        int humidityStatus = checkThreshold(
                threshold.getHumidityActive(),
                data.getHumidity(),
                threshold.getHumidityMin(),
                threshold.getHumidityMax()
        );

        int rainfallStatus = checkThreshold(
                threshold.getRainfallActive(),
                data.getRainfall(),
                threshold.getRainfallMax()
        );

        int windSpeedStatus = checkThreshold(
                threshold.getWindSpeedActive(),
                data.getWindSpeed(),
                threshold.getWindSpeedMax()
        );

        int dustStatus = checkThreshold(
                threshold.getDustActive(),
                data.getDust(),
                threshold.getDustMax()
        );

        if (temperatureStatus == 0 &&
                humidityStatus == 0 &&
                rainfallStatus == 0 &&
                windSpeedStatus == 0 &&
                dustStatus == 0) {
            return null;
        }

        Alert alert = new Alert();
        alert.setMessage(generateMessage(temperatureStatus, humidityStatus, rainfallStatus, windSpeedStatus, dustStatus));
        alert.setStatus(Alert.Status.NEW);
        alert.setCreatedAt(Instant.now());
        alert.setWeatherData(data);

        alertRepository.save(alert);

        Long stationId = alert.getWeatherData().getStation().getId();
        AlertResponse alertResponse = alertMapper.toResponse(alert);
        webSocketService.sendAlert(stationId, alertResponse);

        alert.setStatus(Alert.Status.SENT);
        alertRepository.save(alert);
        return alertResponse;
    }

    private int checkThreshold(boolean active, Float value, Float min, Float max) {
        if (!active) return 0;

        if (min != null && value < min) return -1;

        if (max != null && value > max) return 1;

        return 0;
    }

    private int checkThreshold(boolean active, Float value, Float max) {
        if (!active) return 0;

        if (max != null && value > max) return 1;

        return 0;
    }

    private String generateMessage(int temperature, int humidity, int rainfall, int windSpeed, int dust) {
        List<String> messages = new ArrayList<>();

        if (temperature == -1) messages.add("Temperature below minimum");
        if (temperature == 1) messages.add("Temperature above maximum");

        if (humidity == -1) messages.add("Humidity below minimum");
        if (humidity == 1) messages.add("Humidity above maximum");

        if (rainfall == 1) messages.add("Rainfall above maximum");

        if (windSpeed == 1) messages.add("Wind speed above maximum");

        if (dust == 1) messages.add("Dust level above maximum");

        return String.join(", ", messages);
    }

    @Override
    public AlertResponse getById(Long id) {
        return alertRepository.findById(id)
                .map(alertMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Alert", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AlertResponse> getByUser(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Alert> alerts = alertRepository
                .findByWeatherData_Station_User_Id(userService.getCurrentUser().getId(), pageable);
        List<AlertResponse> content = alerts.getContent()
                .stream()
                .map(alertMapper::toResponse)
                .toList();
        return new PageResponse<>(
                content,
                alerts.getNumber(),
                alerts.getSize(),
                alerts.getTotalElements(),
                alerts.getTotalPages(),
                alerts.isLast()
        );
    }

    @Override
    @Transactional
    public AlertResponse updateStatus(Long id, Alert.Status status) {
        Alert alert = alertRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Alert", "id", id));
        alert.setStatus(status);
        Alert saved = alertRepository.save(alert);
        return alertMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteAlert(Long id) {
        if (!alertRepository.existsById(id)) {
            throw new ResourceNotFoundException("Alert", "id", id);
        }
        alertRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllByUser(Long userId) {
        alertRepository.deleteAllByWeatherData_Station_User_Id(userId);
    }

    public enum ThresholdStatus {
        BELOW_MIN,
        NORMAL,
        ABOVE_MAX
    }

}
