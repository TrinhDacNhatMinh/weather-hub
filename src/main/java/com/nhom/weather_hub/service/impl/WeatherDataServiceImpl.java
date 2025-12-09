package com.nhom.weather_hub.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom.weather_hub.dto.request.WeatherDataRequest;
import com.nhom.weather_hub.dto.response.AlertResponse;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.WeatherDataResponse;
import com.nhom.weather_hub.entity.Station;
import com.nhom.weather_hub.entity.WeatherData;
import com.nhom.weather_hub.exception.ResourceNotFoundException;
import com.nhom.weather_hub.mapper.WeatherDataMapper;
import com.nhom.weather_hub.repository.StationRepository;
import com.nhom.weather_hub.repository.WeatherDataRepository;
import com.nhom.weather_hub.service.AlertService;
import com.nhom.weather_hub.service.WeatherDataService;
import com.nhom.weather_hub.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherDataServiceImpl implements WeatherDataService {

    private final WeatherDataRepository weatherDataRepository;
    private final StationRepository stationRepository;
    private final WeatherDataMapper weatherDataMapper;
    private final AlertService alertService;
    private final WebSocketService webSocketService;
    private final ObjectMapper objectMapper;

    @Override
    public void handleIncomingMqttData(String payload) throws JsonProcessingException {

        WeatherDataRequest request = objectMapper.readValue(payload, WeatherDataRequest.class);


        Instant recordTime = request.getRecordAt();
        Instant now = Instant.now();

        // Discard data older than 30 seconds (Stale data check)
        if (recordTime.isBefore(now.minusSeconds(30))) {
            System.out.println("Ignore: " + payload);
            return;
        }


        // Validate API key
        Station station = stationRepository.findByApiKey(request.getApiKey())
                .orElseThrow(() -> new ResourceNotFoundException("Station", "api key", request.getApiKey()));

        // Persist WeatherData to database
        WeatherData data = weatherDataMapper.toEntity(request);
        data.setStation(station);
        weatherDataRepository.save(data);

        // Check for alert
        AlertResponse alert = alertService.checkAndCreateAlert(data);

        // Push real-time updates via WebSocket
        WeatherDataResponse weatherDataResponse = weatherDataMapper.toResponse(data);
        webSocketService.sendWeatherData(station.getId(), weatherDataResponse);

        if (alert != null) {
            webSocketService.sendAlert(station.getId(), alert);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public WeatherDataResponse getWeatherDataById(Long id) {
        WeatherData weatherData = weatherDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WeatherData not found"));
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
        WeatherData lastest = weatherDataRepository.findFirstByStationIdOrderByRecordAtDesc(stationId)
                .orElseThrow(() -> new RuntimeException("No data found"));
        return weatherDataMapper.toResponse(lastest);
    }

    @Override
    @Transactional
    public void deleteByStation(Long stationId) {
        if (!stationRepository.existsById(stationId)) {
            throw new RuntimeException("Station not found");
        }
        weatherDataRepository.deleteByStationId(stationId);
    }

}
