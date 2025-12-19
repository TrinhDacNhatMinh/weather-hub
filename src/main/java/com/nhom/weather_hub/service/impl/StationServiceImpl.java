package com.nhom.weather_hub.service.impl;

import com.nhom.weather_hub.domain.enums.StationStatus;
import com.nhom.weather_hub.dto.request.AddStationRequest;
import com.nhom.weather_hub.dto.request.UpdateStationRequest;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.StationResponse;
import com.nhom.weather_hub.entity.Station;
import com.nhom.weather_hub.entity.User;
import com.nhom.weather_hub.exception.ResourceNotFoundException;
import com.nhom.weather_hub.exception.business.StationAlreadyAssignedException;
import com.nhom.weather_hub.exception.business.PermissionDeniedException;
import com.nhom.weather_hub.mapper.StationMapper;
import com.nhom.weather_hub.repository.StationRepository;
import com.nhom.weather_hub.service.StationService;
import com.nhom.weather_hub.service.ThresholdService;
import com.nhom.weather_hub.service.UserService;
import com.nhom.weather_hub.util.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final long ONLINE_THRESHOLD_SECONDS = 60;
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
        thresholdService.initializeDefaultThreshold(station.getId());
        return station.getApiKey();
    }

    @Override
    @Transactional
    public List<String> createStationsBatch(int n) {
        List<String> apiKeys = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            apiKeys.add(createStation());
        }
        return apiKeys;
    }

    @Override
    @Transactional
    public StationResponse addStationToUser(AddStationRequest request) {
        Station station = stationRepository.findByApiKey(request.apiKey())
                .orElseThrow(() -> new ResourceNotFoundException("Station not found with api key " + request.apiKey()));
        if (station.getUser() != null) {
            throw new StationAlreadyAssignedException();
        }

        station.setUser(userService.getCurrentUser());
        station.setName(request.name());
        station.setLatitude(request.latitude());
        station.setLongitude(request.longitude());
        station.setActive(true);
        Station updated = stationRepository.save(station);
        return stationMapper.toResponse(updated, getStationStatus(updated.getUpdatedAt()));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<StationResponse> getStationsOfCurrentUser(int page, int size) {
        User currentUser = userService.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Station> stationPage = stationRepository.findByUserId(currentUser.getId(), pageable);
        List<StationResponse> content = stationPage.getContent()
                .stream()
                .map(station -> stationMapper.toResponse(station, getStationStatus(station.getUpdatedAt())))
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
                .map(station -> stationMapper.toResponse(station, getStationStatus(station.getUpdatedAt())))
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
                .map(station -> stationMapper.toResponse(station, getStationStatus(station.getUpdatedAt())))
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
                .map(station -> stationMapper.toResponse(station, getStationStatus(station.getUpdatedAt())))
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
                .orElseThrow(() -> new ResourceNotFoundException("Station not found with id " + id));
        return stationMapper.toResponse(station, getStationStatus(station.getUpdatedAt()));
    }

    @Override
    @Transactional(readOnly = true)
    public StationResponse getStationByApiKey(String apiKey) {
        Station station = stationRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found with api key " + apiKey));
        return stationMapper.toResponse(station, getStationStatus(station.getUpdatedAt()));
    }

    @Override
    public StationStatus getStationStatus(Instant updatedAt) {
        if (updatedAt == null) {
            return StationStatus.OFFLINE;
        }

        Instant now = TimeUtils.nowVn();
        long diffSeconds = Duration.between(updatedAt, now).getSeconds();

        if (diffSeconds <= ONLINE_THRESHOLD_SECONDS) {
            return StationStatus.ONLINE;
        }

        return StationStatus.OFFLINE;
    }

    @Override
    @Transactional
    public StationResponse updateStation(Long id, UpdateStationRequest request) {
        Station existing = stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found with id " + id));
        checkOwnership(existing);
        stationMapper.updateEntity(request, existing);
        Station updated = stationRepository.save(existing);
        return stationMapper.toResponse(updated, getStationStatus(updated.getUpdatedAt()));
    }

    @Override
    @Transactional
    public StationResponse updateStationSharing(Long id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found with id " + id));
        checkOwnership(station);
        station.setIsPublic(!station.getIsPublic());
        Station updated = stationRepository.save(station);
        return stationMapper.toResponse(updated, getStationStatus(updated.getUpdatedAt()));
    }

    @Override
    @Transactional
    public void detachStationFromUser(Long id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Station not found with id " + id));
        checkOwnership(station);

        station.setUser(null);
        station.setActive(false);
        station.setIsPublic(false);

        Station updated = stationRepository.save(station);
    }

    @Override
    @Transactional
    public void deleteStation(Long id) {
        if (!stationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Station not found with id " + id);
        }
        stationRepository.deleteById(id);
    }

    private String generateApiKey() {
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

    private void checkOwnership(Station station) {
        Long currentUserId = userService.getCurrentUser().getId();
        if (station.getUser() == null || !Objects.equals(station.getUser().getId(), currentUserId)) {
            throw new PermissionDeniedException();
        }
    }

}
