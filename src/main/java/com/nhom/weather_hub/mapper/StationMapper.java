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

        Long ownerId = null;
        String ownerName = null;

        if (entity.getUser() != null) {
            ownerId = entity.getUser().getId();
            ownerName = entity.getUser().getName();
        }

        return new StationResponse(
                entity.getId(),
                entity.getName(),
                entity.getLocation(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getApiKey(),
                entity.getCreatedAt(),
                status,
                entity.getActive(),
                entity.getIsPublic(),
                ownerId,
                ownerName
        );
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
