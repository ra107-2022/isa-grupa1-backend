package grupa1.jutjubic.service;

import grupa1.jutjubic.dto.PerformanceDataPoint;
import grupa1.jutjubic.dto.PerformanceStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IPerformanceService {
    public Optional<PerformanceStats> getRealTimeStats(String endpointName);
    public List<PerformanceDataPoint> getPerformanceGraph(String endpointName, LocalDateTime startTime, LocalDateTime endTime, String groupBy);
}
