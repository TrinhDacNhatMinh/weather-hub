package com.nhom.weather_hub.service;

import com.nhom.weather_hub.dto.request.StationRequest;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.StationResponse;

import java.util.List;

public interface StationService {

    public String createStation();

    public List<String> createStations(int n);

    public PageResponse<StationResponse> getStations(int page, int size);

    public PageResponse<StationResponse> getStationsByUser(Long userId, int page, int size);

    public StationResponse getStationById(Long id);

    public StationResponse getStationByApiKey(String apiKey);

    public StationResponse updateStation(Long id, StationRequest updateRequest);

    public void deleteStation(Long id);

}
