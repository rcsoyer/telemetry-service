package org.acme.telemetryservice.application.web.errorhandling;

import jakarta.annotation.Nonnull;
import java.sql.SQLIntegrityConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

import static org.springframework.http.HttpStatus.CONFLICT;

/**
 * Handles exceptions thrown by the application and wraps then with Problem details data structure.
 * <br/> Mostly, the exception handlers are already
 * implemented by the {@link ProblemHandling} interface.
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc9457">Problem Details</a>
 */
@Slf4j
@RestControllerAdvice
class ExceptionHandlerAdvice implements ProblemHandling, SecurityAdviceTrait {

    /**
     * Handles exceptions related to data integrity violations that may happen concurrently.
     * <br/> Mainly is expected here to catch attempts to
     * duplicate data on insert or update operations.
     */
    @ExceptionHandler({
      ConstraintViolationException.class,
      DataIntegrityViolationException.class,
      SQLIntegrityConstraintViolationException.class
    })
    ResponseEntity<Problem> dataIntegrityViolationHandler(final Exception error) {
        log.warn("There was an attempt to create or update data that violates integrity constraints",
                 error);
        final var problem = Problem.builder()
                                   .withCause(toProblem(error))
                                   .withStatus(Status.CONFLICT);

        if (error instanceof DataIntegrityViolationException dataIntegrityException) {
            problem.withDetail(dataIntegrityException.getMostSpecificCause().getMessage());
        } else {
            problem.withDetail("Invalid duplicated data provided");
        }

        return ResponseEntity
                 .status(CONFLICT)
                 .body(problem.build());
    }

    @Override
    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<Problem> handleResponseStatusException(
      @Nonnull final ResponseStatusException error, @Nonnull final NativeWebRequest request) {
        log.warn("There was an error in the application", error);
        final var status = Status.valueOf(error.getStatusCode().value());
        final var problem = Problem.builder()
                                   .withStatus(status)
                                   .withDetail(error.getReason())
                                   .withTitle(status.getReasonPhrase())
                                   .build();
        return ResponseEntity
                 .status(status.getStatusCode())
                 .body(problem);
    }
}
