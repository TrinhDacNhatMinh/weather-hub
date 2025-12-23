package com.nhom.weather_hub.projection;

import java.time.LocalDate;

public interface DailyWeatherSummaryProjection {
    LocalDate getDate();

    Double getMinTemperature();
    Double getMaxTemperature();
    Double getAvgTemperature();

    Double getMinHumidity();
    Double getMaxHumidity();
    Double getAvgHumidity();

    Double getMinWindSpeed();
    Double getMaxWindSpeed();
    Double getAvgWindSpeed();

    Double getMinDust();
    Double getMaxDust();
    Double getAvgDust();

    Double getTotalRainfall();
}
