package org.acme.telemetryservice.domain.service.mapper;

import org.acme.telemetryservice.domain.dto.command.TelemetryData;
import org.acme.telemetryservice.domain.entity.BaseAuditEventEntity;
import org.acme.telemetryservice.domain.entity.IoTDevice;

sealed interface TelemetryMapper<E extends BaseAuditEventEntity,
                                  T extends TelemetryData>
  permits CoffeeMachineTelemetryMapper, FridgeTelemetryMapper, ThermostatTelemetryMapper {

    E from(IoTDevice sourceDevice, T telemetryData);
}
