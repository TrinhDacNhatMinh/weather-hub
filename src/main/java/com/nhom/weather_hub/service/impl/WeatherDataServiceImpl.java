package com.nhom.weather_hub.service.impl;

import com.nhom.weather_hub.dto.request.WeatherDataRequest;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.WeatherDataResponse;
import com.nhom.weather_hub.entity.Station;
import com.nhom.weather_hub.entity.WeatherData;
import com.nhom.weather_hub.mapper.WeatherDataMapper;
import com.nhom.weather_hub.repository.StationRepository;
import com.nhom.weather_hub.repository.WeatherDataRepository;
import com.nhom.weather_hub.service.AlertService;
import com.nhom.weather_hub.service.WeatherDataService;
import com.nhom.weather_hub.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherDataServiceImpl implements WeatherDataService {

    private final WeatherDataRepository weatherDataRepository;
    private final StationRepository stationRepository;
    private final WeatherDataMapper weatherDataMapper;
    private final AlertService alertService;
    private final WebSocketService webSocketService;

    @Override
    @Transactional
    public WeatherDataResponse createWeatherData(WeatherDataRequest request, String apiKey) {
        Station station = stationRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new RuntimeException("Station not found"));
        WeatherData weatherData = WeatherData.builder()
                .temperature(request.getTemperature())
                .humidity(request.getHumidity())
                .windSpeed(request.getWindSpeed())
                .rainfall(request.getRainfall())
                .dust(request.getDust())
                .recordAt(request.getRecordAt())
                .station(station)
                .build();
        weatherDataRepository.save(weatherData);

        alertService.checkAndCreateAlert(weatherData);

        WeatherDataResponse response = weatherDataMapper.toResponse(weatherData);

        webSocketService.sendWeatherData(station.getId(), response);

        return weatherDataMapper.toResponse(weatherData);
    }

    @Override
    @Transactional(readOnly = true)
    public WeatherDataResponse getWeatherDataById(Long id) {
        WeatherData weatherData = weatherDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WeatherData not found"));
        return weatherDataMapper.toResponse(weatherData);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<WeatherDataResponse> getWeatherData(Long stationId, Instant from, Instant to, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WeatherData> weatherDataPage = weatherDataRepository.findByStationIdAndRecordAtBetween(stationId, from, to, pageable);
        List<WeatherDataResponse> content = weatherDataPage.getContent()
                .stream()
                .map(weatherDataMapper::toResponse)
                .toList();
        return new PageResponse<>(
                content,
                weatherDataPage.getNumber(),
                weatherDataPage.getSize(),
                weatherDataPage.getTotalElements(),
                weatherDataPage.getTotalPages(),
                weatherDataPage.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public WeatherDataResponse getLatestWeatherData(Long stationId) {
        WeatherData lastest = weatherDataRepository.findFirstByStationIdOrderByRecordAtDesc(stationId)
                .orElseThrow(() -> new RuntimeException("No data found"));
        return weatherDataMapper.toResponse(lastest);
    }

    @Override
    @Transactional
    public void deleteByStation(Long stationId) {
        if (!stationRepository.existsById(stationId)) {
            throw new RuntimeException("Station not found");
        }
        weatherDataRepository.deleteByStationId(stationId);
    }

}
