package com.nhom.weather_hub.projection;

import java.time.Instant;
import java.time.LocalDate;

    public interface HourWeatherSummaryProjection {
    Instant getHour();
    Double getAvgTemperature();
    Double getAvgHumidity();
    Double getAvgWindSpeed();
    Double getAvgDust();
    Double getTotalRainfall();
}
