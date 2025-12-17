package com.nhom.weather_hub.mapper;

import com.nhom.weather_hub.domain.enums.StationStatus;
import com.nhom.weather_hub.dto.request.UpdateStationRequest;
import com.nhom.weather_hub.dto.response.StationResponse;
import com.nhom.weather_hub.entity.Station;
import org.springframework.stereotype.Component;

@Component
public class StationMapper {

    public StationResponse toResponse(Station entity, StationStatus status) {
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
        stationResponse.setStatus(status);
        stationResponse.setActive(entity.getActive());
        stationResponse.setIsPublic(entity.getIsPublic());
        if (entity.getUser() != null) {
            stationResponse.setOwnerId(entity.getUser().getId());
            stationResponse.setOwnerName(entity.getUser().getName());
        }

        return stationResponse;
    }

    public void updateEntity(UpdateStationRequest request, Station entity) {
        if (request == null) {
            return;
        }

        entity.setName(request.name());
        entity.setLocation(request.location());
        entity.setLatitude(request.latitude());
        entity.setLongitude(request.longitude());
    }

}
