package com.nhom.weather_hub.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom.weather_hub.domain.records.ThresholdEvaluation;
import com.nhom.weather_hub.domain.records.WeatherDataRequest;
import com.nhom.weather_hub.dto.response.*;
import com.nhom.weather_hub.entity.Alert;
import com.nhom.weather_hub.entity.Station;
import com.nhom.weather_hub.entity.User;
import com.nhom.weather_hub.entity.WeatherData;
import com.nhom.weather_hub.event.AlertCreatedEvent;
import com.nhom.weather_hub.event.WeatherDataCreatedEvent;
import com.nhom.weather_hub.exception.ResourceNotFoundException;
import com.nhom.weather_hub.mapper.AlertMapper;
import com.nhom.weather_hub.mapper.WeatherDataMapper;
import com.nhom.weather_hub.projection.DailyWeatherSummaryProjection;
import com.nhom.weather_hub.projection.HourWeatherSummaryProjection;
import com.nhom.weather_hub.projection.StationAvgTemperatureProjection;
import com.nhom.weather_hub.repository.StationRepository;
import com.nhom.weather_hub.repository.WeatherDataRepository;
import com.nhom.weather_hub.service.AlertService;
import com.nhom.weather_hub.service.UserService;
import com.nhom.weather_hub.service.WeatherDataService;
import com.nhom.weather_hub.util.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final AlertService alertService;
    private final UserService userService;

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

        Instant recordTime = request.recordAt();
        Instant now = TimeUtils.nowVn();

        // Discard data older than 30 seconds or more than 5 seconds in the future
        if (recordTime.isBefore(now.minusSeconds(30)) || recordTime.isAfter(now.plusSeconds(5))) {
            log.warn("Ignored invalid MQTT timestamp: {}", payload);
            return;
        }

        // Validate API key
        Optional<Station> stationOptional = stationRepository.findByApiKey(request.apiKey());
        if (stationOptional.isEmpty()) {
            log.warn("Invalid API key {}", request.apiKey());
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
    public WeatherDataResponse getWeatherData(Long id) {
        WeatherData weatherData = weatherDataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Weather data not found with id " + id));
        return weatherDataMapper.toResponse(weatherData);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DailyWeatherSummaryResponse> getDailySummary(Long stationId, int days) {

        Instant to = TimeUtils.nowVn();
        Instant from = to.minus(days, ChronoUnit.DAYS);

        List<DailyWeatherSummaryProjection> projections = weatherDataRepository.findDailySummary(stationId, from, to);

        return projections.stream()
                .map(p -> new DailyWeatherSummaryResponse(
                        p.getDate(),

                        p.getMinTemperature(),
                        p.getMaxTemperature(),
                        p.getAvgTemperature(),

                        p.getMinHumidity(),
                        p.getMaxHumidity(),
                        p.getAvgHumidity(),

                        p.getMinWindSpeed(),
                        p.getMaxWindSpeed(),
                        p.getAvgWindSpeed(),

                        p.getMinDust(),
                        p.getMaxDust(),
                        p.getAvgDust(),

                        p.getTotalRainfall()
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HourWeatherDataSummaryResponse> getHourSummary(Long stationId, int hour) {
        Instant to = TimeUtils.nowVn();
        Instant from = to.minus(hour, ChronoUnit.HOURS);

        List<HourWeatherSummaryProjection> projections = weatherDataRepository.findLast24HoursSummary(stationId, from, to);

        return projections.stream()
                .map(
                        p -> new HourWeatherDataSummaryResponse(
                                p.getHour(),
                                p.getAvgTemperature(),
                                p.getAvgHumidity(),
                                p.getAvgWindSpeed(),
                                p.getAvgDust(),
                                p.getTotalRainfall()
                        ))
                .toList();
    }

    @Override
    public List<StationAvgTemperatureResponse> getAvgTemperature() {
        Instant to = TimeUtils.nowVn();
        Instant from = to.minus(1, ChronoUnit.HOURS);
        User user = userService.getCurrentUser();
        List<StationAvgTemperatureProjection> projections = weatherDataRepository.findAvgTemperatureByTimeRange(user.getId(), from, to);

        return projections.stream()
                .map(
                        p -> new StationAvgTemperatureResponse(
                                p.getId(),
                                p.getName(),
                                p.getLatitude(),
                                p.getLongitude(),
                                p.getAvgTemperature()
                ))
                .toList();
    }

    @Override
    @Transactional
    public void deleteWeatherDataByStation(Long stationId) {
        if (!stationRepository.existsById(stationId)) {
            throw new ResourceNotFoundException("Station not found with id " + stationId);
        }
        weatherDataRepository.deleteByStationId(stationId);
    }

}
