package org.acme.telemetryservice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Immutable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static jakarta.persistence.GenerationType.SEQUENCE;
import static lombok.AccessLevel.PROTECTED;
import static org.apache.commons.lang3.ObjectUtils.allNotNull;

/**
 * Immutable, auditable super entity class for events.
 * <br/> These events that this clas represents, are usually created by message brokers.
 * <br/> Provides common functionalities for reusability and DRY(don't repeat yourself)
 *
 * @implNote It provides a common equals and hashcode
 */
@Getter
@ToString
@Immutable
@MappedSuperclass
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAuditEventEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @CreatedDate
    @Column(name = "date_created")
    private Instant createdAt;

    /**
     * Common implementation for equals and hash code.
     * <br/> If needed children classes may  override it to consider other fields
     */
    @Override
    public boolean equals(final Object other) {
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        final var anotherEvent = (BaseAuditEventEntity) other;
        if (allNotNull(getId(), anotherEvent.getId())) {
            return getId().equals(anotherEvent.getId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
