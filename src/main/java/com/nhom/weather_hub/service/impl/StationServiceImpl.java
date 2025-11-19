package com.nhom.weather_hub.service.impl;

import com.nhom.weather_hub.dto.request.StationRequest;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.StationResponse;
import com.nhom.weather_hub.entity.Station;
import com.nhom.weather_hub.mapper.StationMapper;
import com.nhom.weather_hub.repository.StationRepository;
import com.nhom.weather_hub.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;
    private final StationMapper stationMapper;

    @Override
    public String createStation() {
        Station station = new Station();
        station.setApiKey(generateApiKey());
        station.setCreateAt(Instant.now());
        station.setActive(false);
        stationRepository.save(station);
        return station.getApiKey();
    }

    @Override
    public List<String> createStations(int n) {
        List<String> apiKeys = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            apiKeys.add(createStation());
        }
        return apiKeys;
    }

    @Override
    public PageResponse<StationResponse> getStations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("CreateAt").descending());
        Page<Station> stationPage = stationRepository.findAll(pageable);
        List<StationResponse> content = stationPage.getContent()
                .stream()
                .map(stationMapper::toResponse)
                .toList();
        return new PageResponse<>(
                content,
                stationPage.getNumber(),
                stationPage.getSize(),
                stationPage.getTotalElements(),
                stationPage.getTotalPages(),
                stationPage.isLast()
        );
    }

    @Override
    public PageResponse<StationResponse> getStationsByUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("CreateAt").descending());
        Page<Station> stationPage = stationRepository.findByUserId(userId, pageable);
        List<StationResponse> content = stationPage.getContent()
                .stream()
                .map(stationMapper::toResponse)
                .toList();
        return new PageResponse<>(
                content,
                stationPage.getNumber(),
                stationPage.getSize(),
                stationPage.getTotalElements(),
                stationPage.getTotalPages(),
                stationPage.isLast()
        );
    }

    @Override
    public StationResponse getStationById(Long id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Station not found"));
        return stationMapper.toResponse(station);
    }

    @Override
    public StationResponse getStationByApiKey(String apiKey) {
        Station station = stationRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new RuntimeException("Station not found"));
        return stationMapper.toResponse(station);
    }

    @Override
    public StationResponse updateStation(Long stationId, StationRequest updateRequest) {
        Station existing = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("Station not found"));
        stationMapper.updateEntity(updateRequest, existing);
        Station updated = stationRepository.save(existing);
        return stationMapper.toResponse(updated);
    }

    @Override
    public void deleteStation(Long id) {
        if (!stationRepository.existsById(id)) {
            throw new RuntimeException("Station not found");
        }
        stationRepository.deleteById(id);
    }

    private String generateApiKey() {
        String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        String apiKey;
        do {
            apiKey = random.ints(16, 0, CHAR_POOL.length())
                    .mapToObj(CHAR_POOL::charAt)
                    .collect(StringBuilder::new,
                            StringBuilder::append,
                            StringBuilder::append)
                    .toString()
                    .replaceAll("(.{4})(?!$)", "$1-");
        } while (stationRepository.existsByApiKey(apiKey));
        return apiKey;
    }

}
