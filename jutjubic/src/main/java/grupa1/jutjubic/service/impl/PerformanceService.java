package grupa1.jutjubic.service.impl;

import grupa1.jutjubic.dto.PerformanceStats;
import grupa1.jutjubic.model.PerformanceMetric;
import grupa1.jutjubic.repository.PerformanceMetricRepository;
import grupa1.jutjubic.service.IPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;

public class PerformanceService implements IPerformanceService {
    @Autowired
    private PerformanceMetricRepository performanceRepository;
    @Override
    public Optional<PerformanceStats> getRealTimeStats(String endpointName) {
        LocalDateTime last5Mins = LocalDateTime.now().minusMinutes(5);
        List<PerformanceMetric> recentMetrics = performanceRepository.findByEndpointNameAndTimestampAfter(endpointName, last5Mins);
        if (recentMetrics.isEmpty()) {
            return Optional.empty();
        }
        DoubleSummaryStatistics stats = recentMetrics
                .stream()
                .mapToDouble(metric -> metric.getResponseTimeMs().doubleValue())
                .summaryStatistics();

        return Optional.of(new PerformanceStats(
                stats.getAverage(),
                stats.getMin(),
                stats.getMax(),
                ((Integer)recentMetrics.size()).longValue()));
    }
}
