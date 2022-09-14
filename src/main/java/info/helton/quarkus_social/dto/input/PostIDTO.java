package info.helton.quarkus_social.dto.input;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostIDTO {

    @NotBlank(message = "Text is required")
    private String text;
}
