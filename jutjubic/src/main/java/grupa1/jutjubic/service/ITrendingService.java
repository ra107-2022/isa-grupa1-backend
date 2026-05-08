package grupa1.jutjubic.service;

import grupa1.jutjubic.model.TrendingVideos;

public interface ITrendingService {
    public void executeDailyEtl();
    public TrendingVideos getLatestTop3();
}
