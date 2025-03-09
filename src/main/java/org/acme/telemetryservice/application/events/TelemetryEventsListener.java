package org.acme.telemetryservice.application.events;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.acme.telemetryservice.domain.dto.command.FridgeTelemetryEvent;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@KafkaListener(id = "telemetryGroup", topics = "telemetryEvents")
class TelemetryEventsListener {

    @KafkaHandler
    void fridgeTelemetryEventHandler(final FridgeTelemetryEvent event) {
        log.info("fridge telemetry event={}", event);
    }

/*    @KafkaHandler(isDefault = true)
    void fridgeTelemetryEventHandler(final Object event) {
      //  final Object o = headers.get(KafkaHeaders.KEY);
        log.debug("fridge telemetry event={}", event);
    }*/
}
