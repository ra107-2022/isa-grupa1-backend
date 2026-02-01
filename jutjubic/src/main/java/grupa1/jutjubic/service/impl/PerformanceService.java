package grupa1.jutjubic.service.impl;

import grupa1.jutjubic.dto.PerformanceDataPoint;
import grupa1.jutjubic.dto.PerformanceStats;
import grupa1.jutjubic.model.PerformanceMetric;
import grupa1.jutjubic.repository.PerformanceMetricRepository;
import grupa1.jutjubic.service.IPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
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

    @Override
    public List<PerformanceDataPoint> getPerformanceGraph(String endpointName, LocalDateTime startTime, LocalDateTime endTime, String groupBy) {
        List<PerformanceMetric> metrics = performanceRepository.findByEndpointNameAndTimestampBetween(endpointName, startTime, endTime);
        Map<LocalDateTime, List<PerformanceMetric>> grouped = metrics
                .stream()
                .collect(Collectors.groupingBy(metric -> {
                    LocalDateTime time = metric.getTimestamp();
                    return switch (groupBy) {
                        case "MINUTE" -> time.truncatedTo(ChronoUnit.MINUTES);
                        case "HOUR" -> time.truncatedTo(ChronoUnit.HOURS);
                        case "DAY" -> time.truncatedTo(ChronoUnit.DAYS);
                        default -> time;
                    };
                }));

        return grouped
                .entrySet()
                .stream()
                .map(entry -> {
                    List<PerformanceMetric> group = entry.getValue();
                    double avgResponseTime = group.stream()
                            .mapToLong(PerformanceMetric::getResponseTimeMs)
                            .average()
                            .orElse(0.0);
                    return new PerformanceDataPoint(
                        entry.getKey(),
                        avgResponseTime,
                        ((Integer)group.size()).longValue()
                    );
                })
                .sorted(Comparator.comparing(PerformanceDataPoint::getTimestamp))
                .collect(Collectors.toList());
    }
}
