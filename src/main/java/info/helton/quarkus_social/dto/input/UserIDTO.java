package info.helton.quarkus_social.dto.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserIDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Age is required")
    @Positive(message = "Age must be positive")
    private Integer age;
}
