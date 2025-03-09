package org.acme.telemetryservice.application.events;

import lombok.extern.slf4j.Slf4j;
import org.acme.telemetryservice.domain.dto.command.FridgeTelemetryEvent;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@KafkaListener(id = "telemetryGroup", topics = "telemetryEvents")
class TelemetryEventsListener {

    @KafkaHandler
    void fridgeTelemetryEventHandler(final FridgeTelemetryEvent event) {
        log.info("fridge telemetry event={}", event);
    }
}
