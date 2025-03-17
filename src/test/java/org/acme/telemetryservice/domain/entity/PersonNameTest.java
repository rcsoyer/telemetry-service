package org.acme.telemetryservice.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class PersonNameTest {

    @Test
    void testConstructor() {
        final var name = PersonName.builder()
                                   .initials(" v d ")
                                   .firstName("  van  der  ")
                                   .lastName("  madeweg ")
                                   .build();

        assertEquals("VD", name.getInitials());
        assertEquals("Van der", name.getFirstName());
        assertEquals("Madeweg", name.getLastName());
    }

}