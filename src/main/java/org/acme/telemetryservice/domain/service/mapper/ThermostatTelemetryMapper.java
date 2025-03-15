package org.acme.telemetryservice.domain.service.mapper;

import org.acme.telemetryservice.domain.dto.command.ThermostatTelemetryData;
import org.acme.telemetryservice.domain.entity.ThermostatTelemetryEvent;
import org.mapstruct.Mapper;

@Mapper
public non-sealed interface ThermostatTelemetryMapper
  extends TelemetryMapper<ThermostatTelemetryEvent, ThermostatTelemetryData> {
}