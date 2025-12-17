package com.nhom.weather_hub.listener;

import com.nhom.weather_hub.event.AlertCreatedEvent;
import com.nhom.weather_hub.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlertEventListener {

    private final WebSocketService webSocketService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAlertCreated(AlertCreatedEvent event) {
        try {
            webSocketService.sendAlert(event.stationId(), event.alertResponse());
        } catch (Exception e) {
            log.error("Failed to send alert via WebSocket", e);
        }
    }

}
