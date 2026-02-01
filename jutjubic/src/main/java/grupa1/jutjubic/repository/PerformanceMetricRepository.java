package grupa1.jutjubic.repository;

import grupa1.jutjubic.model.PerformanceMetric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PerformanceMetricRepository extends JpaRepository<PerformanceMetricRepository, Long> {
    List<PerformanceMetric> findAllByEndpointName(String endpointName);
    Optional<PerformanceMetric> save(PerformanceMetric performanceMetric);
    List<PerformanceMetric> findByEndpointNameAndTimestampAfter(String endpointName, LocalDateTime timestamp);
}
