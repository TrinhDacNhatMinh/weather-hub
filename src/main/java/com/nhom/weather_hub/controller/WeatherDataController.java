package com.nhom.weather_hub.controller;

import com.nhom.weather_hub.dto.response.DailyWeatherSummaryResponse;
import com.nhom.weather_hub.dto.response.WeatherDataResponse;
import com.nhom.weather_hub.service.WeatherDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
            @RequestParam(defaultValue = "7") @Min(1) int days
    ) {
        List<DailyWeatherSummaryResponse> responses = weatherDataService.getDailySummary(stationId, days);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/station")
    @Operation(
            summary = "Delete weather data by station ID",
            description = "Delete all weather data records associated with a specific station ID." +
                    "Requires admin permission."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Weather data deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden, admin permission required"),
            @ApiResponse(responseCode = "404", description = "Station or weather data not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteWeatherDataByStation(@RequestParam("stationId") Long stationId) {
        weatherDataService.deleteWeatherDataByStation(stationId);
        return ResponseEntity.noContent().build();
    }

}
