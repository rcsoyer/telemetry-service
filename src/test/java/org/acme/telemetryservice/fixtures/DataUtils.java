package org.acme.telemetryservice.fixtures;

import java.time.LocalDate;
import org.acme.telemetryservice.domain.entity.Account;
import org.acme.telemetryservice.domain.entity.PersonName;
import org.acme.telemetryservice.domain.entity.Principal;

public final class DataUtils {

    private DataUtils() {
    }

    public static Principal principal() {
        return new Principal("username", "password");
    }

    public static PersonName personName() {
        return PersonName
                 .builder()
                 .initials("VD")
                 .firstName("Van der")
                 .lastName("Madeweg")
                 .build();
    }

    public static Account account() {
        return Account.builder()
                      .birthDate(LocalDate.now().minusYears(18))
                      .idDocument("1234567890ETG")
                      .principal(principal())
                      .personName(personName())
                      .build();
    }
}