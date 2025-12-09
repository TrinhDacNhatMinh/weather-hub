package com.nhom.weather_hub.mapper;

import com.nhom.weather_hub.dto.request.WeatherDataRequest;
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

        weatherData.setTemperature(request.getTemperature());
        weatherData.setHumidity(request.getHumidity());
        weatherData.setWindSpeed(request.getWindSpeed());
        weatherData.setRainfall(request.getRainfall());
        weatherData.setDust(request.getDust());
        weatherData.setRecordAt(request.getRecordAt());

        return weatherData;
    }

    public WeatherDataResponse toResponse(WeatherData entity) {
        if (entity == null) {
            return null;
        }

        WeatherDataResponse weatherDataResponse = new WeatherDataResponse();
        weatherDataResponse.setId(entity.getId());
        weatherDataResponse.setTemperature(entity.getTemperature());
        weatherDataResponse.setHumidity(entity.getHumidity());
        weatherDataResponse.setRainfall(entity.getRainfall());
        weatherDataResponse.setWindSpeed(entity.getWindSpeed());
        weatherDataResponse.setDust(entity.getDust());
        weatherDataResponse.setRecordAt(entity.getRecordAt());
        if (entity.getStation() != null) {
            weatherDataResponse.setStationId(entity.getStation().getId());
            weatherDataResponse.setStationName(entity.getStation().getName());
        }

        return weatherDataResponse;
    }

}
