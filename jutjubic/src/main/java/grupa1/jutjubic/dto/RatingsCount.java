package grupa1.jutjubic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingsCount {
    private Long likes;
    private Long dislikes;

    public RatingsCount() {
        likes = 0L;
        dislikes = 0L;
    }
    public RatingsCount(Long likes, Long dislikes) {
        this.likes = likes;
        this.dislikes = dislikes;
    }
}
