package com.nhom.weather_hub.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddStationRequest {

    private String name;

    private String apiKey;

    private BigDecimal latitude;

    private BigDecimal longitude;

}
