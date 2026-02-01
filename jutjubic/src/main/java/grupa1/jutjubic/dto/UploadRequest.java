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
    private Double lat;
    private Double lon;

    public UploadRequest() { super(); }
    public UploadRequest(Long ownerId, String title, String description, List<String> tags, MultipartFile videoFile, MultipartFile thumbnailFile) {
        this.ownerId = ownerId;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.video = videoFile;
        this.thumbnail = thumbnailFile;
        this.lat = 0.0;
        this.lon = 0.0;
    }
    public UploadRequest(Long ownerId, String title, String description, List<String> tags, MultipartFile videoFile, MultipartFile thumbnailFile, Double lat, Double lon) {
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
