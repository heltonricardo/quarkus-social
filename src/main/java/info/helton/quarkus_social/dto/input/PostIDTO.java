package info.helton.quarkus_social.dto.input;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostIDTO {

    @NotBlank(message = "Text is required")
    private String text;
}
