package com.nhom.weather_hub.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Aggregated daily weather summary data, including minimum, maximum, average values " +
        "and total rainfall for a specific day.")
public record DailyWeatherSummaryResponse(
        LocalDate date,

        Double minTemperature,
        Double maxTemperature,
        Double avgTemperature,

        Double minHumidity,
        Double maxHumidity,
        Double avgHumidity,

        Double minWindSpeed,
        Double maxWindSpeed,
        Double avgWindSpeed,

        Double minDust,
        Double maxDust,
        Double avgDust,

        Double totalRainfall
) {
}
