package com.nhom.weather_hub.service;

import com.nhom.weather_hub.domain.enums.StationStatus;
import com.nhom.weather_hub.dto.request.AddStationRequest;
import com.nhom.weather_hub.dto.request.UpdateStationRequest;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.StationResponse;
import com.nhom.weather_hub.entity.User;

import java.time.Instant;
import java.util.List;

public interface StationService {

    String createStation();

    List<String> createStations(int n);

    StationResponse addStation(AddStationRequest request);

    PageResponse<StationResponse> getMyStations(int page, int size);

    PageResponse<StationResponse> getStationsByUserId(Long userId, int page, int size);

    PageResponse<StationResponse> getPublicStations(int page, int size);

    PageResponse<StationResponse> getAllStations(int page, int size);

    StationResponse getStationById(Long id);

    StationResponse getStationByApiKey(String apiKey);

    StationStatus getStatus(Instant updatedAt);

    StationResponse updateStation(Long id, UpdateStationRequest request);

    StationResponse updateStationSharing(Long id);

    StationResponse detachStation(Long id);

    void deleteStation(Long id);

}
