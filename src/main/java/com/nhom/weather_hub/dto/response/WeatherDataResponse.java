package com.nhom.weather_hub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.Instant;

@Data
@Schema(description = "Response containing weather data stored in the system.")
public class WeatherDataResponse {

    @Schema(description = "Unique ID of the weather data record.", example = "123")
    private Long id;

    @Schema(description = "Temperature in Celsius.", example = "28.7")
    private Float temperature;

    @Schema(description = "Humidity percentage (0-100%).", example = "65.2")
    private Float humidity;

    @Schema(description = "Wind speed in m/s.", example = "1.8")
    private Float windSpeed;

    @Schema(description = "Rainfall level in mm.", example = "12")
    private Float rainfall;

    @Schema(description = "Dust concentration (PM value).", example = "42")
    private Float dust;

    @Schema(description = "Timestamp when the data was recorded.", example = "2025-11-20T10:20:00Z")
    private Instant recordAt;

    @Schema(description = "ID of the station where data is recorded.", example = "5")
    private Long stationId;

    @Schema(description = "Name of the station.", example = "Station A - HN")
    private String stationName;

}
