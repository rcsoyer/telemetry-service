package org.acme.telemetryservice.infrastructure.repository;

import java.util.Optional;
import org.acme.telemetryservice.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Query a projection of the data stored in the {@link Account} entity. <br/> It's aimed as a helper to load only the data needed by the Spring
     * Security to authenticate users.
     */
    @Query("SELECT account.principal.username AS username, " +
             "account.principal.password AS password " +
             "FROM Account account " +
             "WHERE account.principal.username = ?1")
    Optional<UserDetails> getPrincipalBy(String username);

    boolean existsAccountByPrincipalUsername(String username);

    Optional<Account> findAccountByPrincipalUsername(String username);
}
