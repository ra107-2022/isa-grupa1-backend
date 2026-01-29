package grupa1.jutjubic.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ViewData {
    Long videoId;
    Long viewerId;

    public ViewData() {}
    public ViewData(Long videoId, Long viewerId) {
        this.videoId = videoId;
        this.viewerId = viewerId;
   }
}
