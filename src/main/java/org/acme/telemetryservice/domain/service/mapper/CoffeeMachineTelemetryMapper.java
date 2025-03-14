package org.acme.telemetryservice.domain.service.mapper;

import org.acme.telemetryservice.domain.dto.command.CoffeeMachineTelemetryData;
import org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent;
import org.acme.telemetryservice.domain.entity.IoTDevice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CoffeeMachineTelemetryMapper {

    @Mapping(target = "sourceDevice", source = "ioTDevice")
    CoffeeMachineTelemetryEvent from(IoTDevice ioTDevice,
                                     CoffeeMachineTelemetryData telemetryData);
}
