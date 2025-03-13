package org.acme.telemetryservice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;

import static lombok.AccessLevel.PROTECTED;

/**
 * Immutable, auditable super entity class.
 * <br/> Differentiates from its superclass in the sense that it may have an actual User that was
 * responsible to trigger the creation of this data
 *
 * @implNote It provides a common equals and hashcode
 */
@Getter
@MappedSuperclass
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
abstract class BaseAuditImmutableEntity extends BaseAuditEventEntity {

    /**
     * username that created the entity
     */
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;
}
