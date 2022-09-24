package info.helton.quarkus_social.dto.output;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowersODTO {
    
    private Integer followersCount;
    private List<FollowerODTO> followers;
}
