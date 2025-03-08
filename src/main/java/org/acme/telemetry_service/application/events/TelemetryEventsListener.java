package org.acme.telemetry_service.application.events;

import org.acme.telemetry_service.domain.dto.command.FridgeTelemetryEvent;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(id = "telemetryGroup", topics = "telemetryEvents")
class TelemetryEventsListener {

    @KafkaHandler
    void fridgeTelemetryEventHandler(final FridgeTelemetryEvent event) {
    }
}
