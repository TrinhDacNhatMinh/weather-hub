package com.nhom.weather_hub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@Schema(description = "Response object representing a weather station.")
public class StationResponse {

    @Schema(description = "Unique identifier of the station.", example = "1")
    private Long id;

    @Schema(description = "Name of the station.", example = "Device A")
    private String name;

    @Schema(description = "Human-readable location of the station.", example = "Ha Noi, Vietnam")
    private String location;

    @Schema(description = "Latitude coordinate of the station.", example = "21.028511")
    private BigDecimal latitude;

    @Schema(description = "Longitude coordinate of the station.", example = "105.804817")
    private BigDecimal longitude;

    @Schema(description = "API key associated with the station.", example = "ABCD-1234-EFGH-5678")
    private String apiKey;

    @Schema(description = "Timestamp when the station was created.", example = "2025-11-19T09:23:19Z")
    private Instant createAt;

    @Schema(description = "Timestamp when the station was last updated.", example = "2025-11-19T10:00:00Z")
    private Instant updateAt;

    @Schema(description = "Whether the station is currently active.", example = "true")
    private Boolean active;

    @Schema(description = "ID of the user who owns the station.", example = "5")
    private Long userId;

}
