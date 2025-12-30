package com.nhom.weather_hub.projection;

import java.math.BigDecimal;

public interface StationAvgTemperatureProjection {
    Long getId();
    String getName();
    BigDecimal getLatitude();
    BigDecimal getLongitude();
    Float getAvgTemperature();
}
