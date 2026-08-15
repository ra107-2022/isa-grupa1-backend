package grupa1.jutjubic.service.impl;

import grupa1.jutjubic.model.TrendingVideos;
import grupa1.jutjubic.repository.TrendingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TrendingService {
    private final TrendingRepository trendingRepository;

    public TrendingService(TrendingRepository trendingRepository) {
        this.trendingRepository = trendingRepository;
    }

    @Scheduled(cron = "0 16 22 * * *")
    public void executeDailyEtl() {
        trendingRepository.runTrendingEtl();
    }

    public TrendingVideos getLatestTop3() {
        return trendingRepository.findTop3();
    }
}
