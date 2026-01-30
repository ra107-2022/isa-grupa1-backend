package grupa1.jutjubic.model;

import grupa1.jutjubic.model.enums.ActivityType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
@Getter
@Setter
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private VideoMetadata video;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private org.locationtech.jts.geom.Point location;

    private LocalDateTime createdAt = LocalDateTime.now();
}
