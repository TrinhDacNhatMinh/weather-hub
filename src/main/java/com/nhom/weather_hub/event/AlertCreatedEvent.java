package com.nhom.weather_hub.event;

import com.nhom.weather_hub.dto.response.AlertResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AlertCreatedEvent {
    private final Long stationId;
    private final AlertResponse alertResponse;
}
