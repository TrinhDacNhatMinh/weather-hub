package com.nhom.weather_hub.controller;

import com.nhom.weather_hub.dto.request.WeatherDataRequest;
import com.nhom.weather_hub.dto.response.PageResponse;
import com.nhom.weather_hub.dto.response.WeatherDataResponse;
import com.nhom.weather_hub.service.WeatherDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/api/weather-data")
@RequiredArgsConstructor
@Tag(name = "Weather Data API", description = "Endpoints for stations to submit and query weather data.")
public class WeatherDataController {

    private final WeatherDataService weatherDataService;

    @PostMapping
    @Operation(summary = "Submit weather data", description = "This endpoint is used by IoT weather stations to send weather data to the backend.")
    public ResponseEntity<WeatherDataResponse> createWeatherData(
            @Valid @RequestBody WeatherDataRequest request,
            @RequestHeader("X-API-KEY") String apiKey) {
        WeatherDataResponse response = weatherDataService.createWeatherData(request, apiKey);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get weather data by ID", description = "Retrieve a single weather data record using its unique ID.")
    public ResponseEntity<WeatherDataResponse> getWeatherDataById(@PathVariable Long id) {
        WeatherDataResponse response = weatherDataService.getWeatherDataById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Query weather data", description = "Retrieve paginated weather data for a specific station.")
    public ResponseEntity<PageResponse<WeatherDataResponse>> getWeatherData(
            @RequestParam Long stationId,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (from == null) {
            from = Instant.EPOCH;
        }
        if (to == null) {
            to = Optional.ofNullable(to).orElse(Instant.parse("9999-12-31T23:59:59Z"));
        }
        PageResponse<WeatherDataResponse> response = weatherDataService.getWeatherData(stationId, from, to, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/latest")
    @Operation(summary = "Get latest weather data", description = "Return the most recent weather data record for the given station.")
    public ResponseEntity<WeatherDataResponse> getLatestWeatherData(@RequestParam Long stationId) {
        WeatherDataResponse response = weatherDataService.getLatestWeatherData(stationId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/station/{stationId}")
    @Operation(summary = "Delete all data for a station", description = "Delete all weather data records belonging to the specified station.")
    public ResponseEntity<Void> deleteByStation(@PathVariable Long stationId) {
        weatherDataService.deleteByStation(stationId);
        return ResponseEntity.noContent().build();
    }

}
