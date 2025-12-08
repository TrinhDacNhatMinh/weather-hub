package com.nhom.weather_hub.mapper;

import com.nhom.weather_hub.dto.response.AlertResponse;
import com.nhom.weather_hub.entity.Alert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertMapper {

    @Mapping(target = "weatherDataId", source = "weatherData.id")
    @Mapping(target = "stationId", source = "weatherData.station.id")
    @Mapping(target = "stationName", source = "weatherData.station.name")
    public AlertResponse toResponse(Alert entity);

}
