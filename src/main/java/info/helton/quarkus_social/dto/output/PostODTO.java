package info.helton.quarkus_social.dto.output;

import java.time.LocalDateTime;

import info.helton.quarkus_social.model.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostODTO {

    private String text;
    private LocalDateTime dateTime;

    public static PostODTO fromEntity(Post post) {
        return PostODTO.builder()
                .text(post.getText())
                .dateTime(post.getDateTime())
                .build();
    }
}
