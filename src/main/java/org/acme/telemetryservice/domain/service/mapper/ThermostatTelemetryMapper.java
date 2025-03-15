package org.acme.telemetryservice.domain.service.mapper;

import org.acme.telemetryservice.domain.dto.command.ThermostatTelemetryData;
import org.acme.telemetryservice.domain.entity.IoTDevice;
import org.acme.telemetryservice.domain.entity.ThermostatTelemetryEvent;
import org.mapstruct.Mapper;

@Mapper
public interface ThermostatTelemetryMapper {

    ThermostatTelemetryEvent from(final IoTDevice sourceDevice,
                                  final ThermostatTelemetryData telemetryData);
}
