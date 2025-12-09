package com.nhom.weather_hub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.time.Instant;

@Data
@Schema(description = "Payload sent by ESP32 containing weather sensor data.")
public class WeatherDataRequest {

    private String apiKey;

    @Schema(description = "Temperature in Celsius.", example = "28.5")
    private Float temperature;

    @DecimalMax(value = "100")
    @DecimalMin(value = "0")
    @Schema(description = "Humidity percentage.", example = "65")
    private Float humidity;

    @DecimalMin(value = "0")
    @Schema(description = "Wind speed in m/s.", example = "1.2")
    private Float windSpeed;

    @DecimalMin(value = "0")
    @Schema(description = "Rainfall amount (mm).", example = "50")
    private Float rainfall;

    @DecimalMin(value = "0")
    @Schema(description = "Dust concentration.", example = "42")
    private Float dust;

    @Schema(description = "Timestamp of the reading.", example = "2025-11-20T10:20:00Z")
    private Instant recordAt;

}
