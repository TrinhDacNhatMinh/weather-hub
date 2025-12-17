package com.nhom.weather_hub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Response containing weather data stored in the system.")
public record WeatherDataResponse(

        @Schema(description = "Unique ID of the weather data record.", example = "123")
        Long id,

        @Schema(description = "Temperature in Celsius.", example = "28.7")
        Float temperature,

        @Schema(description = "Humidity percentage (0-100%).", example = "65.2")
        Float humidity,

        @Schema(description = "Wind speed in m/s.", example = "1.8")
        Float windSpeed,

        @Schema(description = "Rainfall level in mm.", example = "12")
        Float rainfall,

        @Schema(description = "Dust concentration (PM value).", example = "42")
        Float dust,

        @Schema(description = "Timestamp when the data was recorded.", example = "2025-11-20T10:20:00Z")
        Instant recordAt,

        @Schema(description = "ID of the station where data is recorded.", example = "5")
        Long stationId,

        @Schema(description = "Name of the station.", example = "Station A - HN")
        String stationName

) {
}
