package org.acme.telemetryservice.domain.entity;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class PrincipalTest {

    @Test
    void testConstructor() {
        final var username = "    usern ame   ";
        final var password = "    pas  sw  ord";
        final var principal = new Principal(username, password);

        assertEquals("username", principal.getUsername());
        assertEquals("password", principal.getPassword());
    }

}