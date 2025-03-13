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
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static jakarta.persistence.GenerationType.SEQUENCE;
import static lombok.AccessLevel.PROTECTED;
import static org.apache.commons.lang3.ObjectUtils.allNotNull;

@Getter
@ToString
@Immutable
@MappedSuperclass
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
abstract class BaseAuditImmutableEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @CreatedDate
    @Column(name = "date_created", updatable = false)
    private Instant createdAt;

    /**
     * username that created the entity
     */
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;


    @Override
    public boolean equals(final Object other) {
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        final var anotherImmutable = (BaseAuditImmutableEntity) other;
        if (allNotNull(getId(), anotherImmutable.getId())) {
            return getId().equals(anotherImmutable.getId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
