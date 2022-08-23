package info.helton.quarkus_social.error;

import javax.validation.ConstraintViolation;

import lombok.Getter;

@Getter
public class FieldError {

    private final String field;
    private final String message;

    public <T> FieldError(ConstraintViolation<T> violation) {
        field = violation.getPropertyPath().toString();
        message = violation.getMessage();
    }
}
