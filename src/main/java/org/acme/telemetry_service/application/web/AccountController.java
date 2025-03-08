package org.acme.telemetry_service.application.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.telemetry_service.domain.dto.command.AccountRegisterRequest;
import org.acme.telemetry_service.domain.dto.command.AccountRegisterResponse;
import org.acme.telemetry_service.domain.service.AccountService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("accounts")
class AccountController {

    private final AccountService service;

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Register a new account",
      description = "This operation registers a new unique client's Account into the system.")
    @ApiResponse(responseCode = "201", description = "Account successfully registered")
    @ApiResponse(responseCode = "400", description = "Invalid input data",
      content = @Content(mediaType = "application/problem+json",
        schema = @Schema(implementation = Problem.class)))
    @ApiResponse(responseCode = "409", description = "Account username already exists",
      content = @Content(mediaType = "application/problem+json",
        schema = @Schema(implementation = Problem.class)))
    AccountRegisterResponse register(@RequestBody @Valid final AccountRegisterRequest request) {
        log.debug("Rest API call to register a new Account");
        return service.createAccount(request);
    }
}
