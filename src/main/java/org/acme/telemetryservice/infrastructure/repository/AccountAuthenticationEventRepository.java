package org.acme.telemetryservice.infrastructure.repository;

import org.acme.telemetryservice.domain.entity.AccountAuthenticationEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountAuthenticationEventRepository
  extends JpaRepository<AccountAuthenticationEvent, Long> {
}
