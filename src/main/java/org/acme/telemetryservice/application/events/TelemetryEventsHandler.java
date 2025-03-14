package org.acme.telemetryservice.application.events;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.telemetryservice.domain.dto.command.CoffeeMachineTelemetryData;
import org.acme.telemetryservice.domain.dto.command.FridgeTelemetryData;
import org.acme.telemetryservice.domain.dto.command.ThermostatTelemetryData;
import org.acme.telemetryservice.domain.service.FridgeTelemetryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class TelemetryEventsHandler {

    private final FridgeTelemetryService fridgeTelemetryService;

    @KafkaListener(topics = "fridgeEvents")
    void fridgeTelemetryEventHandler(final FridgeTelemetryData event) {
        log.info("fridge telemetry event={}", event);
        fridgeTelemetryService.createTelemetryEvent(event);
    }

    @KafkaListener(topics = "coffeeMachineEvents")
    void coffeeMachineTelemetryEventHandler(@Valid final CoffeeMachineTelemetryData event) {
        log.info("coffee machine telemetry event={}", event);
    }

    @KafkaListener(topics = "thermostatEvents")
    void thermostatTelemetryEventHandler(final ThermostatTelemetryData event) {
        log.info("thermostat telemetry event={}", event);
    }
}
