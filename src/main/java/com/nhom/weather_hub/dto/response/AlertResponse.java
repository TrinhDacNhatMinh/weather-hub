package com.nhom.weather_hub.dto.response;

import lombok.Data;

import java.time.Instant;

@Data
public class AlertResponse {

    private Long id;

    private String message;

    private String status;

    private Instant createdAt;

    private Long weatherDataId;

    private Long stationId;

    private String stationName;

}
