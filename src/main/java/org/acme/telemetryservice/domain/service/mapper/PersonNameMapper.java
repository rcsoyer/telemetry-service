package org.acme.telemetryservice.domain.service.mapper;

import org.acme.telemetryservice.domain.dto.PersonNameDto;
import org.acme.telemetryservice.domain.entity.PersonName;
import org.mapstruct.Mapper;

@Mapper
public interface PersonNameMapper {

    PersonName toEntity(PersonNameDto dto);
}
