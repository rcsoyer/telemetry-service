package org.acme.telemetryservice.infrastructure.repository;

import org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoffeeMachineTelemetryRepository
  extends JpaRepository<CoffeeMachineTelemetryEvent, Long> {
}
