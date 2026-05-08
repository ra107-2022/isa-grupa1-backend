package grupa1.jutjubic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StreamingInfo {
    private Boolean isLive;
    private Long offset;
    private Boolean canSeek;
    private Boolean isAvailable;

    public StreamingInfo() {}
    public StreamingInfo(Boolean isLive, Long offset, Boolean canSeek, Boolean isAvailable) {
        this.isLive = isLive;
        this.offset = offset;
        this.canSeek = canSeek;
        this.isAvailable = isAvailable;
    }
}
