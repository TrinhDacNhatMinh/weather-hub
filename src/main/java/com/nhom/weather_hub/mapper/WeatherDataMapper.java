package com.nhom.weather_hub.mapper;

import com.nhom.weather_hub.domain.records.WeatherDataRequest;
import com.nhom.weather_hub.dto.response.WeatherDataResponse;
import com.nhom.weather_hub.entity.WeatherData;
import org.springframework.stereotype.Component;

@Component
public class WeatherDataMapper {

    public WeatherData toEntity(WeatherDataRequest request) {
        if (request == null) {
            return null;
        }

        WeatherData weatherData = new WeatherData();

        weatherData.setTemperature(request.temperature());
        weatherData.setHumidity(request.humidity());
        weatherData.setWindSpeed(request.windSpeed());
        weatherData.setRainfall(request.rainfall());
        weatherData.setDust(request.dust());
        weatherData.setRecordAt(request.recordAt());

        return weatherData;
    }

    public WeatherDataResponse toResponse(WeatherData entity) {
        if (entity == null) {
            return null;
        }

        Long stationId = null;
        String stationName = null;

        if (entity.getStation() != null) {
            stationId = entity.getStation().getId();
            stationName = entity.getStation().getName();
        }

        return new WeatherDataResponse(
                entity.getId(),
                entity.getTemperature(),
                entity.getHumidity(),
                entity.getWindSpeed(),
                entity.getRainfall(),
                entity.getDust(),
                entity.getRecordAt(),
                stationId,
                stationName
        );
    }

}
