package org.acme.telemetry_service.infrastructure.repository;

import org.acme.telemetry_service.domain.entity.AccountAuthenticationEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountAuthenticationEventRepository
  extends JpaRepository<AccountAuthenticationEvent, Long> {
}
