package grupa1.jutjubic.service.impl;

import grupa1.jutjubic.model.ActivityLog;
import grupa1.jutjubic.model.User;
import grupa1.jutjubic.model.VideoMetadata;
import grupa1.jutjubic.model.enums.ActivityType;
import grupa1.jutjubic.repository.ActivityLogRepository;
import grupa1.jutjubic.repository.UserRepository;
import grupa1.jutjubic.repository.VideoMetadataRepository;
import grupa1.jutjubic.service.IActivityService;
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService implements IActivityService {
    @Autowired
    private ActivityLogRepository activityRepository;

    @Autowired
    private UserRepository  userRepository;

    @Autowired
    private VideoMetadataRepository videoRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public void logActivity(Long videoId, Long userId, ActivityType type, double lon, double lat) {
        if (type == ActivityType.NONE)
            return;

        ActivityLog log = new ActivityLog();

        VideoMetadata videoProxy = videoRepository.getReferenceById(videoId);
        User userProxy = userRepository.getReferenceById(userId);

        log.setVideo(videoProxy);
        log.setUser(userProxy);
        log.setActivityType(type);
        log.setLocation(geometryFactory.createPoint(new Coordinate(lon, lat)));

        activityRepository.save(log);
    }

    @Transactional
    public void removeLike(Long userId, Long videoId) {
        activityRepository.deleteFirstByUserIdAndVideoIdAndActivityType(userId, videoId, ActivityType.LIKE);
    }

    @Transactional
    public void removeDislike(Long userId, Long videoId) {
        activityRepository.deleteFirstByUserIdAndVideoIdAndActivityType(userId, videoId, ActivityType.DISLIKE);
    }

    public List<Long> getTrendingVideos(double lon, double lat, double radius, int limit) {
        return activityRepository.findTopVideoIdsByLocationWithDecay(lon, lat, radius, limit);
    }
}
