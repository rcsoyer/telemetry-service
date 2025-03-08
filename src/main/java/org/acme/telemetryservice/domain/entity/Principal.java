package org.acme.telemetryservice.domain.entity;

import static lombok.AccessLevel.PROTECTED;

import static org.apache.commons.lang3.StringUtils.deleteWhitespace;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.NaturalId;

import java.util.Objects;

/**
 * User's credentials.
 * The User's data strictly related to the authentication process
 */
@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Principal {

    @NaturalId
    @NotBlank(message = "The Account username is mandatory")
    @Size(max = 50, message = "The username must be 50 characters or less")
    private String username;

    @NotBlank(message = "The account password is mandatory")
    @Size(max = 150)
    private String password;

    public Principal(final String username, final String password) {
        this.username = deleteWhitespace(username);
        this.password = deleteWhitespace(password);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        final var that = (Principal) other;

        if (getUsername() == null || that.getUsername() == null) {
            return false;
        }

        return getUsername().equalsIgnoreCase(that.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUsername().toUpperCase());
    }
}
