package info.helton.quarkus_social.dto.output;

import info.helton.quarkus_social.model.Follower;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowerODTO {

    private Long id;
    private String name;

    public static FollowerODTO fromEntity(Follower follower) {
        return FollowerODTO.builder()
                .id(follower.getId())
                .name(follower.getUser().getName())
                .build();
    }
}
