package grupa1.jutjubic.model;

import grupa1.jutjubic.model.enums.RatingType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "RATINGS",
        uniqueConstraints = { @UniqueConstraint(columnNames = {"video_id", "user_id"}) }
)
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "video_id", nullable = false)
    private VideoMetadata videoMetadata;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "user_id", nullable = false)
    private User user;

    @Column (name = "type")
    private RatingType  ratingType;

    @Column (name = "timestamp")
    private LocalDateTime timestamp;

    public Rating() {}
    public Rating(VideoMetadata videoMetadata, User user, RatingType ratingType, LocalDateTime timestamp) {
        this.videoMetadata = videoMetadata;
        this.user = user;
        this.ratingType = ratingType;
        this.timestamp = timestamp;
    }
}
