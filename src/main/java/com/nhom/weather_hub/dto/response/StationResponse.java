package com.nhom.weather_hub.dto.response;

import com.nhom.weather_hub.domain.enums.StationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

@Schema(description = "Response object representing a weather station")
public record StationResponse(

        @Schema(description = "Unique identifier of the station", example = "1")
        Long id,

        @Schema(description = "Name of the station", example = "Device A")
        String name,

        @Schema(description = "Human-readable location of the station", example = "Ha Noi, Vietnam")
        String location,

        @Schema(description = "Latitude coordinate of the station", example = "21.028511")
        BigDecimal latitude,

        @Schema(description = "Longitude coordinate of the station.", example = "105.804817")
        BigDecimal longitude,

        @Schema(description = "API key associated with the station.", example = "LFKN-4FRA-5CWG-YN14")
        String apiKey,

        @Schema(description = "Timestamp when the station was created.", example = "2025-11-19T09:23:19Z")
        Instant createdAt,

        @Schema(description = "Current status of the station", example = "ONLINE")
        StationStatus status,

        @Schema(description = "Whether the station is currently active", example = "true")
        Boolean active,

        @Schema(description = "Whether the station is public", example = "false")
        Boolean isPublic,

        @Schema(description = "ID of the user who owns the station", example = "5")
        Long ownerId,

        @Schema(description = "Name of the station owner", example = "Nguyen Van A")
        String ownerName

) {
}
