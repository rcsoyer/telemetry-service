package org.acme.telemetryservice.domain.dto.command;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ValueOfEnumValidator
  implements ConstraintValidator<EnumType, CharSequence> {

    private List<String> acceptedValues;

    @Override
    public void initialize(final EnumType annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                               .map(Enum::name)
                               .toList();
    }

    @Override
    public boolean isValid(final CharSequence value, final ConstraintValidatorContext context) {
        if (isBlank(value)) {
            return false;
        }

        return acceptedValues.contains(value.toString().toUpperCase());
    }
}
