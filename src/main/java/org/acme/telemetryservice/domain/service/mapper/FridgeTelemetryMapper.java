package org.acme.telemetryservice.domain.service.mapper;

import org.acme.telemetryservice.domain.dto.command.FridgeTelemetryData;
import org.acme.telemetryservice.domain.entity.FridgeTelemetryEvent;
import org.mapstruct.Mapper;

@Mapper
public non-sealed interface FridgeTelemetryMapper
  extends TelemetryMapper<FridgeTelemetryEvent, FridgeTelemetryData> {
}
