package grupa1.jutjubic.dto;

import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class VideoInfo {
    private String title;
    private Long viewCount;
    private String userUsername;
    private Boolean isLive;
    private Long duration;
    private LocalDateTime premiereDate;

    public VideoInfo() {}
    public VideoInfo(String title, Long viewCount, String userUsername, Boolean isLive, Long duration, LocalDateTime premiereDate) {
        this.title = title;
        this.viewCount = viewCount;
        this.userUsername = userUsername;
        this.isLive = isLive;
        this.duration = duration;
        this.premiereDate = premiereDate;
    }
}
