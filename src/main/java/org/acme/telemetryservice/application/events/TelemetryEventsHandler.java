package org.acme.telemetryservice.application.events;

import lombok.extern.slf4j.Slf4j;
import org.acme.telemetryservice.domain.dto.command.CoffeeMachineTelemetryEvent;
import org.acme.telemetryservice.domain.dto.command.FridgeTelemetryEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class TelemetryEventsHandler {

    @KafkaListener(topics = "fridgeEvents")
    void fridgeTelemetryEventHandler(final FridgeTelemetryEvent event) {
        log.info("fridge telemetry event={}", event);
    }

    @KafkaListener(topics = "coffeeMachineEvents")
    void coffeeMachineTelemetryEventHandler(final CoffeeMachineTelemetryEvent event) {
        log.info("coffee machine telemetry event={}", event);
    }
}
