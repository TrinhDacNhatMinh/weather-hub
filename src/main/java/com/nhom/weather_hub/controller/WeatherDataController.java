package com.nhom.weather_hub.controller;

import com.nhom.weather_hub.dto.response.DailyWeatherSummaryResponse;
import com.nhom.weather_hub.dto.response.HourWeatherDataSummaryResponse;
import com.nhom.weather_hub.dto.response.StationAvgTemperatureResponse;
import com.nhom.weather_hub.dto.response.WeatherDataResponse;
import com.nhom.weather_hub.service.WeatherDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weather-data")
@RequiredArgsConstructor
@Tag(name = "Weather Data", description = "Endpoints for querying and managing weather data records")
public class WeatherDataController {

    private final WeatherDataService weatherDataService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Get weather data by ID",
            description = "Retrieve a single weather data record by its unique ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Weather data retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, authentication required"),
            @ApiResponse(responseCode = "404", description = "Weather data not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<WeatherDataResponse> getWeatherData(@PathVariable Long id) {
        WeatherDataResponse response = weatherDataService.getWeatherData(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stations/{stationId}/daily-summary")
    @Operation(
            summary = "Get daily weather summary by station ID",
            description = "Retrieve aggregated daily weather data for a specific station. " +
                    "Each day includes minimum, maximum, and average values of weather metrics " +
                    "and total rainfall, optimized for chart visualization."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Weather data retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request param"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, authentication required"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<DailyWeatherSummaryResponse>> getDailySummary(
            @PathVariable Long stationId,
            @RequestParam(defaultValue = "7") @Min(1) @Max(30) int days
    ) {
        List<DailyWeatherSummaryResponse> responses = weatherDataService.getDailySummary(stationId, days);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/stations/{stationId}/hour-summary")
    @Operation(
            summary = "Get hourly weather summary for the last N hours",
            description = "Retrieve aggregated hourly weather data for a specific station within " +
                    "the last N hours (default is 24 hours). Each hour includes average values of " +
                    "temperature, humidity, wind speed, and dust concentration, along with the " +
                    "total rainfall amount (mm) calculated from rainfall intensity data. " +
                    "This endpoint is optimized for hourly chart visualization."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Weather data retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request param"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, authentication required"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<HourWeatherDataSummaryResponse>> getHourSummary(
            @PathVariable Long stationId,
            @RequestParam(defaultValue = "24") @Min(1) @Max(24) int hour
    ) {
        List<HourWeatherDataSummaryResponse> responses = weatherDataService.getHourSummary(stationId, hour);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/stations/avg-temperature")
    @Operation(
            summary = "Get average temperature of station's current user or station's public in an hour",
            description = "Retrieve the average temperature of a specific weather station. " +
                    "The result is calculated based on weather data records associated " +
                    "with the station and is intended for statistical analysis or chart visualization."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Weather data retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, authentication required"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<StationAvgTemperatureResponse>> getAvgTemperature() {
        List<StationAvgTemperatureResponse> responses = weatherDataService.getAvgTemperature();
        return ResponseEntity.ok(responses);
    }

}
