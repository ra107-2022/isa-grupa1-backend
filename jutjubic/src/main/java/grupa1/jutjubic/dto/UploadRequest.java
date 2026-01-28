package grupa1.jutjubic.dto;

import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public MultipartFile getVideo() {
        return video;
    }

    public void setVideo(MultipartFile video) {
        this.video = video;
    }

    public MultipartFile getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(MultipartFile thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Long getLat() {
        return lat;
    }

    public void setLat(Long lat) {
        this.lat = lat;
    }

    public Long getLon() {
        return lon;
    }

    public void setLon(Long lon) {
        this.lon = lon;
    }
}
