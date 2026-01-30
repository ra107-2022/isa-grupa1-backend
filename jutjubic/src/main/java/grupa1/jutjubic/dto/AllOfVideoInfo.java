package grupa1.jutjubic.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class AllOfVideoInfo {
    private Long ownerId;
    private String ownerUsername;

    private String title;
    private String description;
    private List<String> tags;
    private LocalDateTime publishDate;

    private Long viewCount;
    private Long likeCount;
    private Long dislikeCount;

    public AllOfVideoInfo() {}
    public AllOfVideoInfo(Long ownerId, String ownerUsername, String title, String description, List<String> tags, LocalDateTime publishDate, Long viewCount, Long likeCount, Long dislikeCount) {
        this.ownerId = ownerId;
        this.ownerUsername = ownerUsername;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.publishDate = publishDate;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }
}
