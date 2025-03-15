package org.acme.telemetryservice.infrastructure.repository;

import org.acme.telemetryservice.domain.entity.ThermostatTelemetryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThermostatTelemetryRepository
  extends JpaRepository<ThermostatTelemetryEvent, Long> {
}
