package com.nhom.weather_hub.service.impl;

import com.nhom.weather_hub.dto.request.ThresholdRequest;
import com.nhom.weather_hub.dto.response.ThresholdResponse;
import com.nhom.weather_hub.entity.Station;
import com.nhom.weather_hub.entity.Threshold;
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
        thresholdRepository.findByStationId(stationId).ifPresent(threshold -> {
            throw new RuntimeException("Threshold already exists for this station");
        });

        Threshold threshold = new Threshold();
        threshold.setTemperatureMin(10.0f);
        threshold.setTemperatureMax(35.0f);
        threshold.setHumidityMin(15.0f);
        threshold.setHumidityMax(85.0f);
        threshold.setRainfallMax(100.0f);
        threshold.setWindSpeedMax(5.0f);
        threshold.setDustMax(100.0f);
        threshold.setTemperatureActive(false);
        threshold.setHumidityActive(false);
        threshold.setRainfallActive(false);
        threshold.setWindSpeedActive(false);
        threshold.setDustActive(false);
        Station station = stationRepository.findById(stationId).orElseThrow(() -> new RuntimeException("Station not found"));
        threshold.setStation(station);
        thresholdRepository.save(threshold);
    }

    @Override
    @Transactional(readOnly = true)
    public ThresholdResponse getByStationId(Long stationId) {
        Threshold threshold = thresholdRepository.findByStationId(stationId)
                .orElseThrow(() -> new RuntimeException("Threshold not found"));
        return thresholdMapper.toResponse(threshold);
    }

    @Override
    @Transactional
    public ThresholdResponse updateThreshold(Long id, ThresholdRequest request) {
        Threshold existing = thresholdRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Threshold not found"));
        thresholdMapper.updateEntity(request, existing);
        Threshold updated = thresholdRepository.save(existing);
        return thresholdMapper.toResponse(updated);
    }

}
