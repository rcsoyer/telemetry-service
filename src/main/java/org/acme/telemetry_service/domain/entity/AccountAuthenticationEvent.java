package org.acme.telemetry_service.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationFailureCredentialsExpiredEvent;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;
import static org.acme.telemetry_service.domain.entity.AccountAuthenticationEvent.AuthenticationEventType.FAILURE;
import static org.acme.telemetry_service.domain.entity.AccountAuthenticationEvent.AuthenticationEventType.FAILURE_BAD_CREDENTIALS;
import static org.acme.telemetry_service.domain.entity.AccountAuthenticationEvent.AuthenticationEventType.FAILURE_CREDENTIALS_EXPIRED;
import static org.acme.telemetry_service.domain.entity.AccountAuthenticationEvent.AuthenticationEventType.FAILURE_LOCKED_ACCOUNT;
import static org.acme.telemetry_service.domain.entity.AccountAuthenticationEvent.AuthenticationEventType.FAILURE_USER_NOT_FOUND;
import static org.acme.telemetry_service.domain.entity.AccountAuthenticationEvent.AuthenticationEventType.SUCCESS;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class AccountAuthenticationEvent extends BaseAuditImmutableEntity {

    @ManyToOne
    @JoinColumn(name = "account_id", updatable = false)
    private Account account;

    @NotNull
    @PastOrPresent
    private Instant authenticationTimestamp;

    private InetAddress remoteAddress;

    @NotNull
    @Enumerated(STRING)
    private AuthenticationEventType eventType;

    private AccountAuthenticationEvent(final AbstractAuthenticationEvent event) {
        this.authenticationTimestamp = Instant.ofEpochMilli(event.getTimestamp());
        setRemoteAddress(event);
    }

    private AccountAuthenticationEvent(final Account account,
                                       final AbstractAuthenticationEvent event) {
        this(event);
        this.account = account;
    }

    public AccountAuthenticationEvent(final Account account, final AuthenticationSuccessEvent event) {
        this(account, (AbstractAuthenticationEvent) event);
        this.eventType = SUCCESS;
    }

    public AccountAuthenticationEvent(final Account account,
                                      final AbstractAuthenticationFailureEvent event) {
        this(account, (AbstractAuthenticationEvent) event);

        this.eventType = switch (event) {
            case AuthenticationFailureBadCredentialsEvent ignored -> FAILURE_BAD_CREDENTIALS;
            case AuthenticationFailureCredentialsExpiredEvent ignored -> FAILURE_CREDENTIALS_EXPIRED;
            case AuthenticationFailureLockedEvent ignored -> FAILURE_LOCKED_ACCOUNT;
            default -> FAILURE;
        };
    }

    public AccountAuthenticationEvent(final AbstractAuthenticationFailureEvent event) {
        this((AbstractAuthenticationEvent) event);
        eventType = FAILURE_USER_NOT_FOUND;
    }

    private void setRemoteAddress(final AbstractAuthenticationEvent event) {
        try {
            final String remoteAddress =
              ((WebAuthenticationDetails) event.getAuthentication().getDetails())
                .getRemoteAddress();
            this.remoteAddress = InetAddress.getByName(remoteAddress);
        } catch (final UnknownHostException notIpAddressError) {
            throw new IllegalArgumentException(
              "Invalid IP Address propagated by Spring Security Authentication Event",
              notIpAddressError);
        }
    }

    public enum AuthenticationEventType {
        SUCCESS,
        FAILURE,
        FAILURE_BAD_CREDENTIALS,
        FAILURE_USER_NOT_FOUND,
        FAILURE_CREDENTIALS_EXPIRED,
        FAILURE_LOCKED_ACCOUNT
    }
}
