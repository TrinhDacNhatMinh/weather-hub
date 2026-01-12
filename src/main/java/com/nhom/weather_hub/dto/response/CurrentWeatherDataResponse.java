package com.nhom.weather_hub.dto.response;

import com.nhom.weather_hub.domain.enums.StationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

@Schema(description = "Response containing current weather data from user's and public stations.")
public record CurrentWeatherDataResponse(

        @Schema(description = "Unique ID of the latest weather data record (null if station has no data).", example = "123", nullable = true)
        Long id,

        @Schema(description = "Current temperature in Celsius (latest record).", example = "28.7", nullable = true)
        Float temperature,

        @Schema(description = "Current humidity percentage 0-100% (latest record).", example = "65.2", nullable = true)
        Float humidity,

        @Schema(description = "Average wind speed in m/s (last 10 minutes).", example = "1.8", nullable = true)
        Float windSpeed,

        @Schema(description = "Total rainfall in mm per hour (last 1 hour).", example = "12", nullable = true)
        Float rainfall,

        @Schema(description = "Average dust concentration PM value (last 1 hour).", example = "42", nullable = true)
        Float dust,

        @Schema(description = "Average dust concentration AQI value (last 1 hour).", example = "85", nullable = true)
        Float dustAqi,

        @Schema(description = "Timestamp of the latest weather data record.", example = "2025-11-20T10:20:00Z", nullable = true)
        Instant recordAt,

        @Schema(description = "ID of the station where data is recorded.", example = "5")
        Long stationId,

        @Schema(description = "Name of the station.", example = "Station A - HN")
        String stationName,

        @Schema(description = "Online/Offline status of the station (ONLINE if updated within 60 seconds, otherwise OFFLINE).", example = "ONLINE")
        StationStatus status,

        @Schema(description = "Latitude coordinate of the station.", example = "10.762622")
        BigDecimal latitude,

        @Schema(description = "Longitude coordinate of the station.", example = "106.660172")
        BigDecimal longitude

) {
}
