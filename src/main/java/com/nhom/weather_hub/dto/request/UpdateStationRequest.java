package com.nhom.weather_hub.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Request body used for updating a weather station.")
public class StationRequest {

    @Size(max = 50, message = "Name must be at most 50 characters")
    @Schema(description = "Name of the station.", example = "Device A", nullable = true)
    private String name;

    @Size(max = 100, message = "Location must be at most 100 characters")
    @Schema(description = "Human-readable location of the station.", example = "Ha Noi, Vietnam", nullable = true)
    private String location;

    private BigDecimal latitude;

    private BigDecimal longitude;

}
