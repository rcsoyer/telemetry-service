package org.acme.telemetryservice.domain.entity;

import java.time.LocalDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.acme.telemetryservice.fixtures.DataUtils.personName;
import static org.acme.telemetryservice.fixtures.DataUtils.principal;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

    @Nested
    class TestConstructor {

        @Test
        void testConstructor_whenLegalAgeThenSuccess() {
            final var legalAge = LocalDate.now().minusYears(18);
            final Principal principal = principal();
            final PersonName personName = personName();
            final var account = Account.builder()
                                       .birthDate(legalAge)
                                       .idDocument("  1234567  890ETG  ")
                                       .principal(principal)
                                       .personName(personName)
                                       .build();

            assertEquals(legalAge, account.getBirthDate());
            assertEquals("1234567890ETG", account.getIdDocument());
            assertEquals(principal, account.getPrincipal());
            assertEquals(personName, account.getPersonName());
        }

        @Test
        void testConstructor_whenNotLegalAgeThenError() {
            final var notLegalAge = LocalDate.now().minusYears(17);
            assertThrows(ResponseStatusException.class,
                         () -> Account.builder()
                                      .birthDate(notLegalAge)
                                      .idDocument("1234567890ETG")
                                      .principal(principal())
                                      .personName(personName())
                                      .build());
        }
    }
}