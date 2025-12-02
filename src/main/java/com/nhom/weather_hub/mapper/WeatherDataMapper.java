package com.nhom.weather_hub.mapper;

import com.nhom.weather_hub.dto.request.WeatherDataRequest;
import com.nhom.weather_hub.dto.response.WeatherDataResponse;
import com.nhom.weather_hub.entity.WeatherData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WeatherDataMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "station", ignore = true)
    public WeatherData toEntity(WeatherDataRequest request);

    @Mapping(target = "stationId", source = "station.id")
    @Mapping(target = "stationName", source = "station.name")
    public WeatherDataResponse toResponse(WeatherData entity);

}
