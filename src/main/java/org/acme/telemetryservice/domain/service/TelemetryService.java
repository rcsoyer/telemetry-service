package org.acme.telemetryservice.domain.service;

import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.telemetryservice.domain.dto.command.TelemetryData;
import org.acme.telemetryservice.domain.dto.query.TelemetryEventFilter;
import org.acme.telemetryservice.domain.entity.BaseAuditEventEntity;
import org.acme.telemetryservice.domain.entity.IoTDevice;
import org.acme.telemetryservice.infrastructure.repository.IoTDeviceRepository;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
abstract sealed class TelemetryService<E extends BaseAuditEventEntity,
                                        T extends TelemetryData>
  permits CoffeeMachineTelemetryService, FridgeTelemetryService, ThermostatTelemetryService {

    private final IoTDeviceRepository deviceRepository;

    public abstract void createTelemetryEvent(T event);

    protected final void createTelemetryEvent(T event,
                                              BiFunction<IoTDevice, T, E> mapToEntity,
                                              UnaryOperator<E> persistEvent) {
        deviceRepository
          .findByDeviceId(event.deviceId())
          .map(sourceDevice -> mapToEntity.apply(sourceDevice, event))
          .map(persistEvent)
          .orElseThrow(iotSourceDeviceNotFound(event.deviceId()));
    }

    private Supplier<IllegalArgumentException> iotSourceDeviceNotFound(final UUID sourceId) {
        return () -> {
            log.warn("Invalid deviceId={} passed as event source", sourceId);
            return new IllegalArgumentException("IoT Device not found");
        };
    }

    protected Supplier<ResponseStatusException> notFoundMatchingFilter(
      final TelemetryEventFilter filter) {
        return () -> {
            log.warn("No data found matching filter={}", filter);
            return
              new ResponseStatusException(NOT_FOUND,
                                          "No data found matching provided query parameters");
        };
    }
}
