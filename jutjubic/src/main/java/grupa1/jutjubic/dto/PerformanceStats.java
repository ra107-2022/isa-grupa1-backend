package grupa1.jutjubic.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PerformanceStats {
    private Double avgResponseTime;
    private Double minResponseTime;
    private Double maxResponseTime;
    private Long totalRequests;

    public PerformanceStats() {}
    public PerformanceStats(Double avg, Double min, Double max, Long totalRequests) {
        this.avgResponseTime = avg;
        this.minResponseTime = min;
        this.maxResponseTime = max;
        this.totalRequests = totalRequests;
    }
}
