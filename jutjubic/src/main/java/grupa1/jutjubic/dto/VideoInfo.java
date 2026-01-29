package grupa1.jutjubic.dto;

import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VideoInfo {
    private String title;
    private Long viewCount;
    private String userUsername;

    public VideoInfo() {}
    public VideoInfo(String title, Long viewCount, String userUsername) {
        this.title = title;
        this.viewCount = viewCount;
        this.userUsername = userUsername;
    }
}
