package info.helton.quarkus_social.dto.input;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowerIDTO {

    @NotNull(message = "FollowerId is required")
    @Positive(message = "FollowerId must be a positive number")
    private Long followerId;
}
