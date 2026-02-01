package grupa1.jutjubic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "PERFORMANCE_METRICS")
public class PerformanceMetric {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "response_time_ms", nullable = false)
    private Long responseTimeMs;

    @Column (name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column (name = "video_count", nullable = false)
    private Long videoCount;

    @Column (name = "endpoint_name")
    private String endpointName;

    public PerformanceMetric() {}
    public PerformanceMetric(Long responseTimeMs, LocalDateTime timestamp, Long videoCount, String endpointName) {
        this.responseTimeMs = responseTimeMs;
        this.timestamp = timestamp;
        this.videoCount = videoCount;
        this.endpointName = endpointName;
    }
}
