package org.acme.telemetryservice.domain.service.mapper;

import org.acme.telemetryservice.domain.dto.command.CoffeeMachineTelemetryData;
import org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent;
import org.mapstruct.Mapper;

@Mapper
public non-sealed interface CoffeeMachineTelemetryMapper
  extends TelemetryMapper<CoffeeMachineTelemetryEvent, CoffeeMachineTelemetryData> {
}