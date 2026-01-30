package grupa1.jutjubic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentDto {
    private Long commentId;
    private Long authorId;
    private String authorUsername;
    private Long videoId;
    private String videoTitle;
    private Long videoOwnerId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}