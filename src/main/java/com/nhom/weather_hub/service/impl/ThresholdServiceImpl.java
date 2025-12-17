package com.nhom.weather_hub.service.impl;

import com.nhom.weather_hub.dto.request.UpdateThresholdRequest;
import com.nhom.weather_hub.dto.response.ThresholdResponse;
import com.nhom.weather_hub.entity.Station;
import com.nhom.weather_hub.entity.Threshold;
import com.nhom.weather_hub.exception.ResourceNotFoundException;
import com.nhom.weather_hub.exception.business.ThresholdAlreadyExistsException;
import com.nhom.weather_hub.mapper.ThresholdMapper;
import com.nhom.weather_hub.repository.StationRepository;
import com.nhom.weather_hub.repository.ThresholdRepository;
import com.nhom.weather_hub.service.ThresholdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ThresholdServiceImpl implements ThresholdService {

    private final ThresholdRepository thresholdRepository;
    private final StationRepository stationRepository;
    private final ThresholdMapper thresholdMapper;

    @Override
    @Transactional
    public void createDefaultThreshold(Long stationId) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found with id " + stationId));
        thresholdRepository.findByStationId(stationId).ifPresent(threshold -> {
            throw new ThresholdAlreadyExistsException(stationId);
        });

        Threshold threshold = Threshold.builder()
                .temperatureMin(10f)
                .temperatureMax(35f)
                .humidityMin(15f)
                .humidityMax(85f)
                .rainfallMax(100f)
                .windSpeedMax(5f)
                .dustMax(100f)
                .temperatureActive(false)
                .humidityActive(false)
                .rainfallActive(false)
                .windSpeedActive(false)
                .dustActive(false)
                .station(station)
                .build();

        thresholdRepository.save(threshold);
    }

    @Override
    @Transactional(readOnly = true)
    public ThresholdResponse getByStationId(Long stationId) {
        Threshold threshold = thresholdRepository.findByStationId(stationId)
                .orElseThrow(() -> new ResourceNotFoundException("Threshold not found with station id " + stationId));
        return thresholdMapper.toResponse(threshold);
    }

    @Override
    @Transactional
    public ThresholdResponse updateThreshold(Long id, UpdateThresholdRequest request) {
        Threshold existing = thresholdRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Threshold not found with id " + id));
        thresholdMapper.updateEntity(request, existing);
        Threshold updated = thresholdRepository.save(existing);
        return thresholdMapper.toResponse(updated);
    }

}
