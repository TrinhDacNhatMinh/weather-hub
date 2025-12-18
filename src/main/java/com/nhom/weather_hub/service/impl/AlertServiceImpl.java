package com.nhom.weather_hub.service.impl;

import com.nhom.weather_hub.domain.enums.AlertStatus;
import com.nhom.weather_hub.domain.enums.ThresholdStatus;
import com.nhom.weather_hub.domain.records.ThresholdEvaluation;
import com.nhom.weather_hub.dto.response.AlertResponse;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.entity.Alert;
import com.nhom.weather_hub.entity.Threshold;
import com.nhom.weather_hub.entity.WeatherData;
import com.nhom.weather_hub.exception.ResourceNotFoundException;
import com.nhom.weather_hub.mapper.AlertMapper;
import com.nhom.weather_hub.repository.AlertRepository;
import com.nhom.weather_hub.repository.ThresholdRepository;
import com.nhom.weather_hub.service.AlertService;
import com.nhom.weather_hub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;
    private final ThresholdRepository thresholdRepository;
    private final AlertMapper alertMapper;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public Optional<ThresholdEvaluation> evaluateThresholds(WeatherData data) {
        Threshold threshold = thresholdRepository.findByStationId(data.getStation().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Threshold not found"));

        ThresholdStatus temperatureStatus = checkThreshold(
                        threshold.getTemperatureActive(),
                        data.getTemperature(),
                        threshold.getTemperatureMin(),
                        threshold.getTemperatureMax()
        );
        ThresholdStatus humidityStatus = checkThreshold(
                threshold.getHumidityActive(),
                data.getHumidity(),
                threshold.getHumidityMin(),
                threshold.getHumidityMax()
        );
        ThresholdStatus windSpeedStatus = checkThreshold(
                threshold.getWindSpeedActive(),
                data.getWindSpeed(),
                threshold.getWindSpeedMax()
        );
        ThresholdStatus rainfallStatus = checkThreshold(
                threshold.getRainfallActive(),
                data.getRainfall(),
                threshold.getRainfallMax()
        );
        ThresholdStatus dustStatus = checkThreshold(
                threshold.getDustActive(),
                data.getDust(),
                threshold.getDustMax()
        );

        boolean hasAlert = temperatureStatus != ThresholdStatus.NORMAL ||
                        humidityStatus != ThresholdStatus.NORMAL ||
                        windSpeedStatus != ThresholdStatus.NORMAL ||
                        rainfallStatus != ThresholdStatus.NORMAL ||
                        dustStatus != ThresholdStatus.NORMAL;

        if (!hasAlert) return Optional.empty();

        ThresholdEvaluation evaluation = new ThresholdEvaluation(temperatureStatus, humidityStatus, windSpeedStatus, rainfallStatus, dustStatus);
        return Optional.of(evaluation);
    }

    private ThresholdStatus checkThreshold(boolean active, Float value, Float min, Float max) {
        if (!active || value == null) return ThresholdStatus.NORMAL;
        if (value < min) return ThresholdStatus.BELOW_MIN;
        if (value > max) return ThresholdStatus.ABOVE_MAX;
        return ThresholdStatus.NORMAL;
    }

    private ThresholdStatus checkThreshold(boolean active, Float value, Float max) {
        if (!active || value == null) return ThresholdStatus.NORMAL;
        if (value > max) return ThresholdStatus.ABOVE_MAX;
        return ThresholdStatus.NORMAL;
    }


    @Override
    @Transactional
    public Alert createAlert(WeatherData data, ThresholdEvaluation evaluation) {
        Alert alert = new Alert();
        alert.setMessage(generateMessage(evaluation));
        alert.setStatus(AlertStatus.NEW);
        alert.setCreatedAt(Instant.now());
        alert.setWeatherData(data);
        alertRepository.save(alert);
        return alert;
    }

    private String generateMessage(ThresholdEvaluation evaluation) {
        List<String> messages = new ArrayList<>();

        if (evaluation.temperature() == ThresholdStatus.BELOW_MIN) {
            messages.add("Temperature below minimum");
        } else if (evaluation.temperature() == ThresholdStatus.ABOVE_MAX) {
            messages.add("Temperature above maximum");
        }

        if (evaluation.humidity() == ThresholdStatus.BELOW_MIN) {
            messages.add("Humidity below minimum");
        } else if (evaluation.humidity() == ThresholdStatus.ABOVE_MAX) {
            messages.add("Humidity above maximum");
        }

        if (evaluation.windSpeed() == ThresholdStatus.ABOVE_MAX) {
            messages.add("Wind speed above maximum");
        }

        if (evaluation.rainfall() == ThresholdStatus.ABOVE_MAX) {
            messages.add("Rainfall above maximum");
        }

        if (evaluation.dust() == ThresholdStatus.ABOVE_MAX) {
            messages.add("Dust above maximum");
        }

        return String.join(", ", messages);

    }

    @Override
    @Transactional(readOnly = true)
    public AlertResponse getById(Long id) {
        return alertRepository.findById(id)
                .map(alertMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id " + id));
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
    public AlertResponse updateStatus(Long id, AlertStatus status) {
        Alert alert = alertRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Alert not found with id " + id));
        alert.setStatus(status);
        Alert saved = alertRepository.save(alert);
        return alertMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteAlert(Long id) {
        if (!alertRepository.existsById(id)) {
            throw new ResourceNotFoundException("Alert not found with id " + id);
        }
        alertRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllByUser(Long userId) {
        alertRepository.deleteAllByWeatherData_Station_User_Id(userId);
    }

}
