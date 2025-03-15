package org.acme.telemetryservice.domain.service.mapper;

import org.acme.telemetryservice.domain.dto.command.ThermostatTelemetryData;
import org.acme.telemetryservice.domain.entity.IoTDevice;
import org.acme.telemetryservice.domain.entity.ThermostatTelemetryEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ThermostatTelemetryMapper {

    @Mapping(target = "sourceDevice", source = "ioTDevice")
    ThermostatTelemetryEvent from(
      final IoTDevice ioTDevice,
      final ThermostatTelemetryData telemetryData);
}
