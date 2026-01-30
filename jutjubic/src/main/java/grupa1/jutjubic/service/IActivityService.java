package grupa1.jutjubic.service;

import grupa1.jutjubic.model.enums.ActivityType;

import java.util.List;

public interface IActivityService {
    void logActivity(Long videoId, Long userId, ActivityType type, double lon, double lat);
    List<Long> getTrendingVideos(double lon, double lat, double radius, int limit);
    void removeLike(Long userId, Long videoId);
    void removeDislike(Long userId, Long videoId);
}
