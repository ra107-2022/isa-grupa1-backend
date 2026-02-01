package grupa1.jutjubic.repository;

import grupa1.jutjubic.model.PerformanceMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceMetricRepository extends JpaRepository<PerformanceMetric, Long> {
    List<PerformanceMetric> findAllByEndpointName(String endpointName);
    List<PerformanceMetric> findByEndpointNameAndTimestampAfter(String endpointName, LocalDateTime timestamp);
    List<PerformanceMetric> findByEndpointNameAndTimestampBetween(String endpointName, LocalDateTime start, LocalDateTime end);
}
