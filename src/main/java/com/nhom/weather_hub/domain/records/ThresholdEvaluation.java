package com.nhom.weather_hub.domain.records;

import com.nhom.weather_hub.domain.enums.ThresholdStatus;

public record ThresholdEvaluation(
        ThresholdStatus temperature,
        ThresholdStatus humidity,
        ThresholdStatus windSpeed,
        ThresholdStatus rainfall,
        ThresholdStatus dust
) {
}
