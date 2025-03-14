package org.acme.telemetryservice.infrastructure.repository;

import jakarta.persistence.QueryHint;
import java.util.Optional;
import java.util.UUID;
import org.acme.telemetryservice.domain.entity.IoTDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface IoTDeviceRepository extends JpaRepository<IoTDevice, Long> {

    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Optional<IoTDevice> findByDeviceId(UUID deviceId);

    boolean existsByDeviceName(String deviceName);
}
