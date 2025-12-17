package com.nhom.weather_hub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object representing threshold configuration of a weather station")
public record ThresholdResponse(

        @Schema(description = "Threshold ID", example = "48")
        Long id,

        @Schema(description = "Minimum temperature threshold (°C)", example = "10.0")
        Float temperatureMin,

        @Schema(description = "Maximum temperature threshold (°C)", example = "35.0")
        Float temperatureMax,

        @Schema(description = "Minimum humidity threshold (%)", example = "20.0")
        Float humidityMin,

        @Schema(description = "Maximum humidity threshold (%)", example = "80.0")
        Float humidityMax,

        @Schema(description = "Maximum rainfall threshold (mm)", example = "50.0")
        Float rainfallMax,

        @Schema(description = "Maximum wind speed threshold (m/s)", example = "5.0")
        Float windSpeedMax,

        @Schema(description = "Maximum dust concentration threshold (µg/m³)", example = "100.0")
        Float dustMax,

        @Schema(description = "Whether temperature threshold alert is active", example = "true")
        Boolean temperatureActive,

        @Schema(description = "Whether humidity threshold alert is active", example = "true")
        Boolean humidityActive,

        @Schema(description = "Whether rainfall threshold alert is active", example = "false")
        Boolean rainfallActive,

        @Schema(description = "Whether wind speed threshold alert is active", example = "true")
        Boolean windSpeedActive,

        @Schema(description = "Whether dust threshold alert is active", example = "true")
        Boolean dustActive,

        @Schema(description = "ID of the station this threshold belongs to", example = "12")
        Long stationId

) {
}
