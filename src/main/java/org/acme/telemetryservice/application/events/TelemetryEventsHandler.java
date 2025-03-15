package org.acme.telemetryservice.application.events;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.telemetryservice.domain.dto.command.CoffeeMachineTelemetryData;
import org.acme.telemetryservice.domain.dto.command.FridgeTelemetryData;
import org.acme.telemetryservice.domain.dto.command.ThermostatTelemetryData;
import org.acme.telemetryservice.domain.service.CoffeeMachineTelemetryService;
import org.acme.telemetryservice.domain.service.FridgeTelemetryService;
import org.acme.telemetryservice.domain.service.ThermostatTelemetryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class TelemetryEventsHandler {

    private final FridgeTelemetryService fridgeTelemetryService;
    private final CoffeeMachineTelemetryService coffeeMachineTelemetryService;
    private final ThermostatTelemetryService thermostatTelemetryService;

    @KafkaListener(topics = "fridgeEvents")
    void fridgeTelemetryEventHandler(@Valid final FridgeTelemetryData event) {
        log.info("fridge telemetry event={}", event);
        fridgeTelemetryService.createTelemetryEvent(event);
    }

    @KafkaListener(topics = "coffeeMachineEvents")
    void coffeeMachineTelemetryEventHandler(@Valid final CoffeeMachineTelemetryData event) {
        log.info("coffee machine telemetry event={}", event);
        coffeeMachineTelemetryService.createTelemetryEvent(event);
    }

    @KafkaListener(topics = "thermostatEvents")
    void thermostatTelemetryEventHandler(@Valid final ThermostatTelemetryData event) {
        log.info("Thermostat telemetry event={}", event);
        thermostatTelemetryService.createTelemetryEvent(event);
    }
}
