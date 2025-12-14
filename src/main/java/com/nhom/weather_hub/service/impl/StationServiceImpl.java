package com.nhom.weather_hub.service.impl;

import com.nhom.weather_hub.dto.request.AddStationRequest;
import com.nhom.weather_hub.dto.request.UpdateStationRequest;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.StationResponse;
import com.nhom.weather_hub.entity.Station;
import com.nhom.weather_hub.entity.User;
import com.nhom.weather_hub.mapper.StationMapper;
import com.nhom.weather_hub.repository.StationRepository;
import com.nhom.weather_hub.service.StationService;
import com.nhom.weather_hub.service.ThresholdService;
import com.nhom.weather_hub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;
    private final StationMapper stationMapper;
    private final ThresholdService thresholdService;
    private final UserService userService;

    @Override
    @Transactional
    public String createStation() {
        Station station = new Station();
        station.setApiKey(generateApiKey());
        station.setCreatedAt(Instant.now());
        station.setActive(false);
        station.setIsPublic(false);
        stationRepository.save(station);
        thresholdService.createDefaultThreshold(station.getId());
        return station.getApiKey();
    }

    @Override
    @Transactional
    public List<String> createStations(int n) {
        List<String> apiKeys = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            apiKeys.add(createStation());
        }
        return apiKeys;
    }

    @Override
    @Transactional
    public StationResponse addStation(AddStationRequest request) {
        Station station = stationRepository.findByApiKey(request.getApiKey())
                .orElseThrow(() -> new RuntimeException("Station not found"));
        if (station.getUser() != null) {
            throw new RuntimeException("Station already assigned to another user");
        }

        station.setUser(userService.getCurrentUser());
        station.setName(request.getName());
        station.setLatitude(request.getLatitude());
        station.setLongitude(request.getLongitude());
        station.setActive(true);
        Station updated = stationRepository.save(station);
        return stationMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<StationResponse> getMyStations(int page, int size) {
        User currentUser = userService.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Station> stationPage = stationRepository.findByUserId(currentUser.getId(), pageable);
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
    @Transactional(readOnly = true)
    public PageResponse<StationResponse> getStationsByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
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
    @Transactional(readOnly = true)
    public PageResponse<StationResponse> getPublicStations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Station> stationPage = stationRepository.findByIsPublicTrue(pageable);
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
    @Transactional(readOnly = true)
    public PageResponse<StationResponse> getAllStations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
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
    @Transactional(readOnly = true)
    public StationResponse getStationById(Long id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Station not found"));
        return stationMapper.toResponse(station);
    }

    @Override
    @Transactional(readOnly = true)
    public StationResponse getStationByApiKey(String apiKey) {
        Station station = stationRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new RuntimeException("Station not found"));
        return stationMapper.toResponse(station);
    }

    @Override
    @Transactional
    public StationResponse updateStation(Long id, UpdateStationRequest request) {
        Station existing = stationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Station not found"));
        if (!(Objects.equals(existing.getUser().getId(), userService.getCurrentUser().getId()))) {
            throw new RuntimeException("You do not have permission to detach this station");
        }
        stationMapper.updateEntity(request, existing);
        Station updated = stationRepository.save(existing);
        return stationMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public StationResponse updateStationSharing(Long id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Station not found"));
        if (!(Objects.equals(station.getUser().getId(), userService.getCurrentUser().getId()))) {
            throw new RuntimeException("You do not have permission to detach this station");
        }
        station.setIsPublic(!station.getIsPublic());
        Station updated = stationRepository.save(station);
        return stationMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public StationResponse detachStation(Long id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Station not found"));
        if (!(Objects.equals(station.getUser().getId(), userService.getCurrentUser().getId()))) {
            throw new RuntimeException("You do not have permission to detach this station");
        }
        station.setUser(null);
        Station updated = stationRepository.save(station);
        return stationMapper.toResponse(updated);
    }

    @Override
    @Transactional
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
