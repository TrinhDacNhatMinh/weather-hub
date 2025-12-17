package com.nhom.weather_hub.event;

import com.nhom.weather_hub.dto.response.AlertResponse;

public record AlertCreatedEvent(
        Long stationId,
        AlertResponse alertResponse
) {
}
