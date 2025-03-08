package org.acme.telemetry_service.domain.service.mapper;

import org.acme.telemetry_service.domain.dto.command.AccountRegisterRequest;
import org.acme.telemetry_service.domain.dto.command.AccountRegisterResponse;
import org.acme.telemetry_service.domain.entity.Account;
import org.acme.telemetry_service.domain.entity.Principal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {PersonNameMapper.class}, imports = {Principal.class})
public interface AccountMapper {

    @Mapping(target = "principal", expression = "java(new Principal(request.username(), password))")
    Account toEntity(AccountRegisterRequest request, String password);

    @Mapping(target = "username", source = "account.principal.username")
    AccountRegisterResponse toResponse(Account account, String password);
}
