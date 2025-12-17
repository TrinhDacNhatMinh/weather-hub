package com.nhom.weather_hub.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom.weather_hub.domain.records.ThresholdEvaluation;
import com.nhom.weather_hub.dto.request.WeatherDataRequest;
import com.nhom.weather_hub.dto.response.AlertResponse;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.WeatherDataResponse;
import com.nhom.weather_hub.entity.Alert;
import com.nhom.weather_hub.entity.Station;
import com.nhom.weather_hub.entity.WeatherData;
import com.nhom.weather_hub.event.AlertCreatedEvent;
import com.nhom.weather_hub.event.WeatherDataCreatedEvent;
import com.nhom.weather_hub.exception.ResourceNotFoundException;
import com.nhom.weather_hub.mapper.AlertMapper;
import com.nhom.weather_hub.mapper.WeatherDataMapper;
import com.nhom.weather_hub.repository.StationRepository;
import com.nhom.weather_hub.repository.WeatherDataRepository;
import com.nhom.weather_hub.service.AlertService;
import com.nhom.weather_hub.service.WeatherDataService;
import com.nhom.weather_hub.util.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherDataServiceImpl implements WeatherDataService {

    private final WeatherDataRepository weatherDataRepository;
    private final StationRepository stationRepository;
    private final WeatherDataMapper weatherDataMapper;
    private final AlertMapper alertMapper;
    private final AlertService alertService;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public void handleIncomingMqttData(String payload) {
        WeatherDataRequest request;

        try {
            request = objectMapper.readValue(payload, WeatherDataRequest.class);
        } catch (JsonProcessingException exception) {
            log.warn("Invalid MQTT payload JSON: {}", payload);
            return;
        }

        Instant recordTime = request.getRecordAt();
        Instant now = TimeUtils.nowVn();

        // Discard data older than 30 seconds or more than 5 seconds in the future
        if (recordTime.isBefore(now.minusSeconds(30)) || recordTime.isAfter(now.plusSeconds(5))) {
            log.warn("Ignored invalid MQTT timestamp: {}", payload);
            return;
        }

        // Validate API key
        Optional<Station> stationOptional = stationRepository.findByApiKey(request.getApiKey());
        if (stationOptional.isEmpty()) {
            log.warn("Invalid API key {}", request.getApiKey());
            return;
        }
        Station station = stationOptional.get();

        // Verify active
        if (!station.getActive()) {
            log.warn("Station {} is inactive. Ignoring data.", station.getId());
            return;
        }

        // Persist WeatherData to database
        WeatherData data = weatherDataMapper.toEntity(request);
        data.setStation(station);
        weatherDataRepository.save(data);

        // Update station updatedAt
        station.setUpdatedAt(TimeUtils.nowVn());
        stationRepository.save(station);

        // Push real-time updates via WebSocket
        WeatherDataResponse weatherDataResponse = weatherDataMapper.toResponse(data);
        applicationEventPublisher.publishEvent(new WeatherDataCreatedEvent(station.getId(), weatherDataResponse));

        // Alert
        Optional<ThresholdEvaluation> evaluation = alertService.evaluateThresholds(data);
        if (evaluation.isPresent()) {
            Alert alert = alertService.createAlert(data, evaluation.get());
            AlertResponse alertResponse = alertMapper.toResponse(alert);
            applicationEventPublisher.publishEvent(new AlertCreatedEvent(station.getId(), alertResponse));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public WeatherDataResponse getWeatherDataById(Long id) {
        WeatherData weatherData = weatherDataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Weather data not found with id " + id));
        return weatherDataMapper.toResponse(weatherData);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<WeatherDataResponse> getWeatherData(Long stationId, Instant from, Instant to, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WeatherData> weatherDataPage = weatherDataRepository.findByStationIdAndRecordAtBetween(stationId, from, to, pageable);
        List<WeatherDataResponse> content = weatherDataPage.getContent()
                .stream()
                .map(weatherDataMapper::toResponse)
                .toList();
        return new PageResponse<>(
                content,
                weatherDataPage.getNumber(),
                weatherDataPage.getSize(),
                weatherDataPage.getTotalElements(),
                weatherDataPage.getTotalPages(),
                weatherDataPage.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public WeatherDataResponse getLatestWeatherData(Long stationId) {
        WeatherData latest = weatherDataRepository.findFirstByStationIdOrderByRecordAtDesc(stationId)
                .orElseThrow(() -> new ResourceNotFoundException("No data found"));
        return weatherDataMapper.toResponse(latest);
    }

    @Override
    @Transactional
    public void deleteByStation(Long stationId) {
        if (!stationRepository.existsById(stationId)) {
            throw new ResourceNotFoundException("Station not found with id " + stationId);
        }
        weatherDataRepository.deleteByStationId(stationId);
    }

}
