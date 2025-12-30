package com.nhom.weather_hub.dto.response;

import java.time.Instant;

public record HourWeatherDataSummaryResponse(
        Instant hour,
        Double avgTemperature,
        Double avgHumidity,
        Double avgWindSpeed,
        Double avgDust,
        Double totalRainfall
) {
}
