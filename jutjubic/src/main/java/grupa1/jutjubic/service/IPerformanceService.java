package grupa1.jutjubic.service;

import grupa1.jutjubic.dto.PerformanceStats;

import java.util.Optional;

public interface IPerformanceService {
    public Optional<PerformanceStats> getRealTimeStats(String endpointName);
}
