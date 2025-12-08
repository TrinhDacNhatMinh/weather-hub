package com.nhom.weather_hub.dto.request;

import lombok.Data;

@Data
public class ThresholdRequest {

    private Float temperatureMin;

    private Float temperatureMax;

    private Float humidityMin;

    private Float humidityMax;

    private Float rainfallMax;

    private Float windSpeedMax;

    private Float dustMax;

    private Boolean temperatureActive;

    private Boolean humidityActive;

    private Boolean rainfallActive;

    private Boolean windSpeedActive;

    private Boolean dustActive;

}
