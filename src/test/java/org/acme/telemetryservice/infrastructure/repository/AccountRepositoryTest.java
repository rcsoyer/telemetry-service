package org.acme.telemetryservice.infrastructure.repository;

import java.util.Optional;
import org.acme.telemetryservice.domain.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import static org.acme.telemetryservice.fixtures.DataUtils.account;
import static org.assertj.core.api.Assertions.assertThat;

class AccountRepositoryTest extends BaseRepositoryTest {

    private Account defaultAccount;

    @Autowired
    private AccountRepository repository;

    @BeforeEach
    void setUp() {
        final Account accountHolder = account();
        defaultAccount = repository.save(accountHolder);
    }

    @Test
    void getPrincipalBy() {
        final String username = defaultAccount.getPrincipal().getUsername();
        final Optional<UserDetails> result = repository.getPrincipalBy(username);

        assertThat(result)
          .get()
          .matches(userDetails -> userDetails.getUsername().equals(username));
    }
}