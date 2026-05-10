package grupa1.jutjubic.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UploadEventDto {
    private String title;
    private Long size;
    private String author;
    private String filename;
    private Long timestamp;

    public UploadEventDto() {}

    public UploadEventDto(String title, Long size, String author, String filename, Long timestamp) {
        this.title = title;
        this.size = size;
        this.author = author;
        this.filename = filename;
        this.timestamp = timestamp;
    }
}
