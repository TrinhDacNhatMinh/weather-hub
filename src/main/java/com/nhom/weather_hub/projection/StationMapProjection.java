package com.nhom.weather_hub.projection;

import java.math.BigDecimal;

public interface StationMapProjection {
    Long getStationId();
    String getStationName();
    BigDecimal getLatitude();
    BigDecimal getLongitude();
    Float getTemperature();
    Float getHumidity();
    Float getWindSpeed();
    Float getTotalRainfall();
    Float getDust();
}
