package com.nhom.weather_hub.mapper;

import com.nhom.weather_hub.dto.request.StationRequest;
import com.nhom.weather_hub.dto.response.StationResponse;
import com.nhom.weather_hub.entity.Station;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "latitude", ignore = true)
    @Mapping(target = "longitude", ignore = true)
    @Mapping(target = "apiKey", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "user", ignore = true)
    public Station toEntity(StationRequest request);

    @Mapping(target = "userId", source = "user.id")
    public StationResponse toResponse(Station entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "apiKey", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "user", ignore = true)
    public void updateEntity(StationRequest request, @MappingTarget Station entity);

}
