package grupa1.jutjubic.metrics;

import grupa1.jutjubic.repository.UserRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ActiveUserMetricsService {

    private final UserRepository userRepository;
    private final AtomicInteger activeUsersGauge;

    public ActiveUserMetricsService(MeterRegistry meterRegistry, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.activeUsersGauge = meterRegistry.gauge("jutjubic.users.active.24h", new AtomicInteger(0));
    }

    @Scheduled(fixedRate = 60000)
    public void updateActiveUsersCount() {
        Instant twentyFourHoursAgo = Instant.now().minus(24, ChronoUnit.HOURS);

        int count = userRepository.countByLastActiveAtAfter(twentyFourHoursAgo);

        activeUsersGauge.set(count);
    }
}