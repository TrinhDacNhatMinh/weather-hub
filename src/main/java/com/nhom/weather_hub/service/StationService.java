package com.nhom.weather_hub.service;

import com.nhom.weather_hub.domain.enums.StationStatus;
import com.nhom.weather_hub.dto.request.AddStationRequest;
import com.nhom.weather_hub.dto.request.UpdateStationRequest;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.StationMapResponse;
import com.nhom.weather_hub.dto.response.StationResponse;

import java.time.Instant;
import java.util.List;

public interface StationService {

    String createStation();

    List<String> createStationsBatch(int n);

    StationResponse addStationToUser(AddStationRequest request);

    PageResponse<StationResponse> getStationsOfCurrentUser(int page, int size);

    PageResponse<StationResponse> getStationsByUserId(Long userId, int page, int size);

    PageResponse<StationResponse> getPublicStations(int page, int size);

    PageResponse<StationResponse> getAllStations(int page, int size);

    StationResponse getStationById(Long id);

    StationResponse getStationByApiKey(String apiKey);

    StationStatus getStationStatus(Instant updatedAt);

    List<StationMapResponse> getStationsForMap(boolean includePublic);

    StationResponse updateStation(Long id, UpdateStationRequest request);

    StationResponse updateStationSharing(Long id);

    void detachStationFromUser(Long id);

    void deleteStation(Long id);

}
