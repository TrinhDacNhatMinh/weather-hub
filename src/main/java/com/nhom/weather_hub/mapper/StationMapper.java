package com.nhom.weather_hub.mapper;

import com.nhom.weather_hub.dto.request.UpdateStationRequest;
import com.nhom.weather_hub.dto.response.StationResponse;
import com.nhom.weather_hub.entity.Station;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StationMapper {

    @Mapping(target = "userId", source = "user.id")
    public StationResponse toResponse(Station entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "apiKey", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "isPublic", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "threshold", ignore = true)
    @Mapping(target = "weatherDataList", ignore = true)
    public void updateEntity(UpdateStationRequest request, @MappingTarget Station entity);

}
