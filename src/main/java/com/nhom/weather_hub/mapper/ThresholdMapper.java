package com.nhom.weather_hub.mapper;

import com.nhom.weather_hub.dto.request.ThresholdRequest;
import com.nhom.weather_hub.dto.response.ThresholdResponse;
import com.nhom.weather_hub.entity.Threshold;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ThresholdMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "station", ignore = true)
    public Threshold toEntity(ThresholdRequest request);

    @Mapping(target = "stationId", source = "station.id")
    public ThresholdResponse toResponse(Threshold entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "station", ignore = true)
    public void updateEntity(ThresholdRequest request, @MappingTarget Threshold entity);

}
