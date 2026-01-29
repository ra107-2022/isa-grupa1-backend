package grupa1.jutjubic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table (
        name = "VIDEO_VIEW",
        uniqueConstraints = { @UniqueConstraint(columnNames = {"video_id", "viewer_id"}) }
)
public class VideoView {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name = "video_id", nullable = false)
    private VideoMetadata videoMetadata;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name = "viewer_id", nullable = false)
    private User user;

    @Column (name = "viewed_at")
    private LocalDateTime viewedAt;

    public VideoView() { }
    public VideoView(VideoMetadata metadata, User user, LocalDateTime time) {
        this.videoMetadata = metadata;
        this.user = user;
        this.viewedAt = time;
    }
}
