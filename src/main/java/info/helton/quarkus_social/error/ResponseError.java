package info.helton.quarkus_social.error;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseError {

    private final String message;
    private final Collection<FieldError> errors;

    public static <T> ResponseError create(String message, Set<ConstraintViolation<T>> violations) {
        List<FieldError> errors = violations.stream().map(FieldError::new).collect(Collectors.toList());
        return new ResponseError(message, errors);
    }
}
