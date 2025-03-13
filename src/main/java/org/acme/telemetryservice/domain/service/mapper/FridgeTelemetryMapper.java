package org.acme.telemetryservice.domain.service.mapper;

import org.acme.telemetryservice.domain.dto.command.FridgeTelemetryData;
import org.acme.telemetryservice.domain.entity.FridgeTelemetryEvent;
import org.acme.telemetryservice.domain.entity.IoTDevice;
import org.mapstruct.Mapper;

@Mapper
public interface FridgeTelemetryMapper {

    FridgeTelemetryEvent from(final IoTDevice sourceDevice,
                              final FridgeTelemetryData telemetryData);
}
