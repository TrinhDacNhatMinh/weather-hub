package com.nhom.weather_hub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;

@Schema(description = "Threshold configuration")
public record UpdateThresholdRequest(

        @Schema(description = "Minimum temperature threshold (°C)", example = "10.0", nullable = true, type = "number", format = "float")
        Float temperatureMin,

        @Schema(description = "Maximum temperature threshold (°C)", example = "35.0", nullable = true, type = "number", format = "float")
        Float temperatureMax,

        @DecimalMin(value = "0.0", message = "Minimum humidity must be >= 0")
        @DecimalMax(value = "100.0", message = "Minimum humidity must be <= 100")
        @Schema(description = "Minimum humidity threshold (%)", example = "20.0", nullable = true, type = "number", format = "float")
        Float humidityMin,

        @DecimalMin(value = "0.0", message = "Minimum humidity must be >= 0")
        @DecimalMax(value = "100.0", message = "Minimum humidity must be <= 100")
        @Schema(description = "Maximum humidity threshold (%)", example = "80.0", nullable = true, type = "number", format = "float")
        Float humidityMax,

        @Positive(message = "Maximum rainfall must be positive if provided")
        @Schema(description = "Maximum rainfall threshold (mm)", example = "50.0", nullable = true, type = "number", format = "float")
        Float rainfallMax,

        @Positive(message = "Maximum wind speed must be positive if provided")
        @Schema(description = "Maximum wind speed threshold (m/s)", example = "5.0", nullable = true, type = "number", format = "float")
        Float windSpeedMax,

        @Positive(message = "Maximum dust concentration must be positive if provided")
        @Schema(description = "Maximum dust concentration threshold (µg/m³)", example = "100.0", nullable = true, type = "number", format = "float")
        Float dustMax,

        @Schema(description = "Enable temperature threshold check", example = "true", nullable = true)
        Boolean temperatureActive,

        @Schema(description = "Enable humidity threshold check", example = "false", nullable = true)
        Boolean humidityActive,

        @Schema(description = "Enable rainfall threshold check", example = "true", nullable = true)
        Boolean rainfallActive,

        @Schema(description = "Enable wind speed threshold check", example = "false", nullable = true)
        Boolean windSpeedActive,

        @Schema(description = "Enable dust threshold check", example = "true", nullable = true)
        Boolean dustActive

) {
}
