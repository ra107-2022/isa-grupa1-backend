package grupa1.jutjubic.repository;
import grupa1.jutjubic.model.ActivityLog;
import grupa1.jutjubic.model.enums.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    @Query(value = """
    SELECT video_id 
    FROM activity_logs 
    WHERE ST_DWithin(location, ST_SetSRID(ST_Point(:lon, :lat), 4326)::geography, :radius)
      AND created_at > NOW() - INTERVAL '48 hours'
    GROUP BY video_id
    ORDER BY SUM(
        (CASE 
            WHEN activity_type = 'LIKE' THEN 50.0
            WHEN activity_type = 'DISLIKE' THEN -50.0
            WHEN activity_type = 'COMMENT' THEN 30.0 
            WHEN activity_type = 'VIEW' THEN 10.0
            ELSE 10.0 
         END) / 
         POWER(EXTRACT(EPOCH FROM (NOW() - created_at))/3600 + 2, 1.5)
    ) DESC
    LIMIT :limit
    """, nativeQuery = true)
    List<Long> findTopVideoIdsByLocationWithDecay(@Param("lon") double lon,
                                                  @Param("lat") double lat,
                                                  @Param("radius") double radius,
                                                  @Param("limit") int limit);

    void deleteFirstByUserIdAndVideoIdAndActivityType(Long userId, Long videoId, ActivityType activityType);
}
