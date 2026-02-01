package grupa1.jutjubic.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PerformanceDataPoint {
    private LocalDateTime timestamp;
    private Double avgResponseTimeMs;
    private Long requestCount;

    public PerformanceDataPoint() {}
    public PerformanceDataPoint(LocalDateTime timestamp, Double avgResponseTimeMs, Long requestCount) {
        this.timestamp = timestamp;
        this.avgResponseTimeMs = avgResponseTimeMs;
        this.requestCount = requestCount;
    }
}
