package com.nhom.weather_hub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Update station request")
public record UpdateStationRequest(

        @Size(min = 1, max = 50, message = "Station name must be between 1 and 50 characters if provided")
        @Schema(description = "Station name", example = "Station B", nullable = true)
        String name,

        @Size(min = 1, message = "Location must not be empty if provided")
        @Schema(description = "Location description of the station", example = "Hoc vien Ky thuat Mat Ma, Ha Noi", nullable = true)
        String location,

        @Schema(description = "Latitude of station", example = "10.762622", nullable = true)
        BigDecimal latitude,

        @Schema(description = "Longitude of station", example = "106.66072", nullable = true)
        BigDecimal longitude

) {
}
