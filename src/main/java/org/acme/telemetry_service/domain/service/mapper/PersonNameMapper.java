package org.acme.telemetry_service.domain.service.mapper;

import org.acme.telemetry_service.domain.dto.PersonNameDto;
import org.acme.telemetry_service.domain.entity.PersonName;
import org.mapstruct.Mapper;

@Mapper
public interface PersonNameMapper {

    PersonName toEntity(PersonNameDto dto);
}
