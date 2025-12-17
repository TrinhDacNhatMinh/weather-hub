package com.nhom.weather_hub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Add station request")
public record AddStationRequest(

        @NotBlank(message = "Station name cannot be blank")
        @Size(max = 50)
        @Schema(description = "Station name", example = "Station A")
        String name,

        @NotBlank(message = "Api key cannot be blank")
        @Schema(description = "API key of the station", example = "LFKN-4FRA-5CWG-YN14")
        String apiKey,

        @NotNull(message = "Latitude cannot be null")
        @Schema(description = "Latitude of station", example = "10.762622")
        BigDecimal latitude,

        @NotNull(message = "Longitude cannot be null")
        @Schema(description = "Longitude of station", example = "106.66072")
        BigDecimal longitude

) {
}
