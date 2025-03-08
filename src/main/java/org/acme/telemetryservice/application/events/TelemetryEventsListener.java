package org.acme.telemetryservice.application.events;

import lombok.extern.slf4j.Slf4j;
import org.acme.telemetryservice.domain.dto.command.FridgeTelemetryEvent;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@KafkaListener(id = "telemetryGroup", topics = "telemetryEvents")
class TelemetryEventsListener {

    @KafkaHandler
    void fridgeTelemetryEventHandler(@Payload final FridgeTelemetryEvent event) {
        log.debug("fridge telemetry event={}", event);
    }
}
