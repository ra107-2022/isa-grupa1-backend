package grupa1.jutjubic.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
public class UploadRequest {
    private Long ownerId;
    private String title;
    private String description;
    private List<String> tags;
    private MultipartFile video;
    private MultipartFile thumbnail;
    private Long lat;
    private Long lon;

    public UploadRequest() { super(); }
    public UploadRequest(Long ownerId, String title, String description, List<String> tags, MultipartFile videoFile, MultipartFile thumbnailFile) {
        this.ownerId = ownerId;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.video = videoFile;
        this.thumbnail = thumbnailFile;
        this.lat = 0L;
        this.lon = 0L;
    }
    public UploadRequest(Long ownerId, String title, String description, List<String> tags, MultipartFile videoFile, MultipartFile thumbnailFile, Long lat, Long lon) {
        this.ownerId = ownerId;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.video = videoFile;
        this.thumbnail = thumbnailFile;
        this.lat = lat;
        this.lon = lon;
    }
}
