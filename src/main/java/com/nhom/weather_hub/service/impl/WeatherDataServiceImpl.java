package com.nhom.weather_hub.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom.weather_hub.dto.request.WeatherDataRequest;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.WeatherDataResponse;
import com.nhom.weather_hub.entity.Station;
import com.nhom.weather_hub.entity.WeatherData;
import com.nhom.weather_hub.event.WeatherDataCreatedEvent;
import com.nhom.weather_hub.exception.ResourceNotFoundException;
import com.nhom.weather_hub.mapper.WeatherDataMapper;
import com.nhom.weather_hub.repository.StationRepository;
import com.nhom.weather_hub.repository.WeatherDataRepository;
import com.nhom.weather_hub.service.AlertService;
import com.nhom.weather_hub.service.WeatherDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    private final AlertService alertService;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleIncomingMqttData(String payload) throws JsonProcessingException {

        WeatherDataRequest request = objectMapper.readValue(payload, WeatherDataRequest.class);

        Instant recordTime = request.getRecordAt();
        Instant now = Instant.now();

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

        // Push real-time updates via WebSocket
        WeatherDataResponse weatherDataResponse = weatherDataMapper.toResponse(data);
        applicationEventPublisher.publishEvent(new WeatherDataCreatedEvent(station.getId(), weatherDataResponse));

        // Check for alert
        try {
            alertService.checkAndCreateAlert(data);
        } catch (Exception ex) {
            log.error("Failed to check alert for station {}: {}", station.getId(), ex.getMessage());
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
