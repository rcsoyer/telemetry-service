package org.acme.telemetryservice.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.telemetryservice.domain.dto.command.AccountRegisterRequest;
import org.acme.telemetryservice.domain.dto.command.AccountRegisterResponse;
import org.acme.telemetryservice.domain.entity.Account;
import org.acme.telemetryservice.domain.service.mapper.AccountMapper;
import org.acme.telemetryservice.infrastructure.repository.AccountRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.acme.telemetryservice.domain.service.PasswordUtils.generateRandomPassword;
import static org.springframework.http.HttpStatus.CONFLICT;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public AccountRegisterResponse createAccount(final AccountRegisterRequest request) {
        log.debug("Registering a new User's Account");
        checkUsernameAvailability(request.username());
        final String rawPassword = generateRandomPassword();
        final String encodedPassword = passwordEncoder.encode(rawPassword);
        final Account account = mapper.toEntity(request, encodedPassword);
        setSecurityContext(account);
        accountRepository.save(account);
        return mapper.toResponse(account, rawPassword);
    }

    private void checkUsernameAvailability(final String username) {
        log.debug("Check if the username is available");
        if (accountRepository.existsAccountByPrincipalUsername(username)) {
            throw new ResponseStatusException(CONFLICT, "The username is already in use");
        }
    }

    private void setSecurityContext(final Account account) {
        log.debug("Setting the Security Context for the new Account");
        final String username = account.getPrincipal().getUsername();
        final String password = account.getPrincipal().getPassword();
        SecurityContextHolder
          .getContext()
          .setAuthentication(new UsernamePasswordAuthenticationToken(username, password));
    }

}
