package grupa1.jutjubic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table (
        name="VIDEO_METADATA",
        uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "video_title"}) }
)
public class VideoMetadata {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn (name ="user_id", nullable = false)
    private User user;

    @Column (name = "upload_date", nullable = false)
    private LocalDateTime uploadDate;

    @Column (name = "video_title", nullable = false)
    private String videoTitle;

    @Column (name = "description", nullable = false)
    private String description;

    @Column (name = "tags", nullable = false)
    private String tags;

    @Column (name = "video_file_name", nullable = false)
    private String videoFileName;

    @Column (name = "video_size", nullable = false)
    private Long videoSize;

    @Column (name = "video_og_file_name")
    private String videoOriginalFileName;

    @Column (name = "thumbnail_file_name", nullable = false)
    private String thumbnailFileName;

    @Column (name = "thumbnail_size", nullable = false)
    private Long thumbnailSize;

    @Column (name = "thumbnail_og_file_name")
    private String thumbnailOriginalFileName;

    @Column (name = "latitude", nullable = true)
    private Long lat;

    @Column (name = "longitude", nullable = true)
    private Long lon;

    @Column (name = "guest_views", nullable = false)
    private Long guestViews = 0L;

    public VideoMetadata() { super(); }
    public VideoMetadata(User user, LocalDateTime uploadDate, String videoTitle, String description, String tags, String videoFileName, Long videoSize, String videoOriginalFileName, String thumbnailFileName, Long thumbnailSize, String thumbnailOriginalFileName, Long lat, Long lon) {
        super();
        this.user = user;
        this.uploadDate = uploadDate;
        this.videoTitle = videoTitle;
        this.description = description;
        this.tags = tags;
        this.videoFileName = videoFileName;
        this.videoSize = videoSize;
        this.videoOriginalFileName = videoOriginalFileName;
        this.thumbnailFileName = thumbnailFileName;
        this.thumbnailSize = thumbnailSize;
        this.thumbnailOriginalFileName = thumbnailOriginalFileName;
        this.lat = lat;
        this.lon = lon;
    }
}
