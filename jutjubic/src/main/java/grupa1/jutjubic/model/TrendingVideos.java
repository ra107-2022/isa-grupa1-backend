package grupa1.jutjubic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "TRENDING_VIDEOS")
public class TrendingVideos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "video_1_id", nullable = false)
    private VideoMetadata video1;

    @Column(name = "score_1", nullable = false)
    private Double score1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "video_2_id", nullable = false)
    private VideoMetadata video2;

    @Column(name = "score_2", nullable = false)
    private Double score2;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "video_3_id", nullable = false)
    private VideoMetadata video3;

    @Column(name = "score_3", nullable = false)
    private Double score3;

    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;

    public TrendingVideos() { }

    public TrendingVideos(VideoMetadata video1, VideoMetadata video2, VideoMetadata video3,
                          Double score1, Double score2, Double score3, LocalDateTime calculatedAt) {
        this.video1 = video1;
        this.video2 = video2;
        this.video3 = video3;
        this.score1 = score1;
        this.score2 = score2;
        this.score3 = score3;
        this.calculatedAt = calculatedAt;
    }
}