package com.nhom.weather_hub.mapper;

import com.nhom.weather_hub.dto.request.UpdateStationRequest;
import com.nhom.weather_hub.dto.response.StationResponse;
import com.nhom.weather_hub.entity.Station;
import org.springframework.stereotype.Component;

@Component
public class StationMapper {

    public StationResponse toResponse(Station entity) {
        if (entity == null) {
            return null;
        }

        StationResponse stationResponse = new StationResponse();
        stationResponse.setId(entity.getId());
        stationResponse.setName(entity.getName());
        stationResponse.setLocation(entity.getLocation());
        stationResponse.setLatitude(entity.getLatitude());
        stationResponse.setLongitude(entity.getLongitude());
        stationResponse.setApiKey(entity.getApiKey());
        stationResponse.setCreatedAt(entity.getCreatedAt());
        stationResponse.setUpdatedAt(entity.getUpdatedAt());
        stationResponse.setActive(entity.getActive());
        stationResponse.setIsPublic(entity.getIsPublic());
        if (entity.getUser() != null) {
            stationResponse.setUserId(entity.getUser().getId());
        }

        return stationResponse;
    }

    public void updateEntity(UpdateStationRequest request, Station entity) {
        if (request == null) {
            return;
        }

        entity.setName(request.getName());
        entity.setLocation(request.getLocation());
        entity.setLatitude(request.getLatitude());
        entity.setLongitude(request.getLongitude());
    }

}
