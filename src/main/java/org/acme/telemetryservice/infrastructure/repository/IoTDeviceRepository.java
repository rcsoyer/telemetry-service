package org.acme.telemetryservice.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;
import org.acme.telemetryservice.domain.entity.IoTDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IoTDeviceRepository extends JpaRepository<IoTDevice, Long> {

    Optional<IoTDevice> findByDeviceId(UUID deviceId);
}
