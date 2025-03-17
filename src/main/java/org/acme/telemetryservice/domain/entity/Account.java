package org.acme.telemetryservice.domain.entity;

import jakarta.persistence.Entity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.Period;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import static java.time.LocalDate.now;
import static lombok.AccessLevel.PROTECTED;
import static org.apache.commons.lang3.StringUtils.deleteWhitespace;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * A person's Account details in the platform.
 * <br/> It's a registry of a person's account details.
 */
@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Account extends BaseAuditEntity {

    @Valid
    @NotNull(message = "A person's name is mandatory")
    private PersonName personName;

    @Past(message = "The birthdate must be in the past")
    @NotNull(message = "A person's birthdate is mandatory")
    private LocalDate birthDate;

    @Size(max = 150, message = "The ID document must be 150 characters or less")
    @NotBlank(message = "A person's ID document is mandatory")
    private String idDocument;

    @Valid
    @NotNull(message = "The account credentials are mandatory")
    private Principal principal;

    @Builder
    private Account(final PersonName personName,
                    final LocalDate birthDate,
                    final String idDocument,
                    final Principal principal) {
        this.personName = personName;
        setBirthDate(birthDate);
        setIdDocument(idDocument);
        this.principal = principal;
    }

    private void setBirthDate(final LocalDate birthDate) {
        if (birthDate != null) {
            final int personsAge = Period.between(birthDate, now()).getYears();
            final boolean isAdult = personsAge >= 18;

            if (!isAdult) {
                throw new ResponseStatusException(
                  BAD_REQUEST,
                  "The person must be an adult to open an account");
            }
        }

        this.birthDate = birthDate;
    }

    private void setIdDocument(final String idDocument) {
        this.idDocument = deleteWhitespace(idDocument);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        final var that = (Account) other;

        if (getPrincipal() == null || that.getPrincipal() == null) {
            return false;
        }

        return getPrincipal().equals(that.getPrincipal());
    }

    @Override
    public int hashCode() {
        return getPrincipal().hashCode();
    }
}
