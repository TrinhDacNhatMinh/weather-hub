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

    public String createStation();

    public List<String> createStations(int n);

    public StationResponse addStation(AddStationRequest request);

    public PageResponse<StationResponse> getMyStations(int page, int size);

    public PageResponse<StationResponse> getStationsByUserId(Long userId, int page, int size);

    public PageResponse<StationResponse> getPublicStations(int page, int size);

    public PageResponse<StationResponse> getAllStations(int page, int size);

    public StationResponse getStationById(Long id);

    public StationResponse getStationByApiKey(String apiKey);

    public StationStatus getStatus(Instant updatedAt);

    public StationResponse updateStation(Long id, UpdateStationRequest request);

    public StationResponse updateStationSharing(Long id);

    public StationResponse detachStation(Long id);

    public void deleteStation(Long id);

}
