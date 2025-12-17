package com.nhom.weather_hub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Response object for weather alerts")
public record AlertResponse(

        @Schema(description = "Alert ID", example = "17")
        Long id,

        @Schema(description = "Alert message", example = "Dust above maximum")
        String message,

        @Schema(description = "Alert status", example = "SEEN")
        String status,

        @Schema(description = "Time alert was created", example = "2025-12-17T08:00:00Z")
        Instant createdAt,

        @Schema(description = "Associated weather data ID", example = "1025")
        Long weatherDataId,

        @Schema(description = "Station ID", example = "12")
        Long stationId,

        @Schema(description = "Station name", example = "Station A")
        String stationName

) {
}
