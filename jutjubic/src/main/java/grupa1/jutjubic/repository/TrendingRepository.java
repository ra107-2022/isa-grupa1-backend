package grupa1.jutjubic.repository;

import grupa1.jutjubic.model.TrendingVideos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TrendingRepository extends JpaRepository<TrendingVideos, Long> {
    @Modifying
    @Transactional
    @Query(value = """
    INSERT INTO trending_videos (video_1_id, score_1, video_2_id, score_2, video_3_id, score_3, calculated_at)
    SELECT 
        COALESCE(MAX(CASE WHEN rank = 1 THEN video_id END), -1),
        COALESCE(MAX(CASE WHEN rank = 1 THEN total_score END), 0.0),
        COALESCE(MAX(CASE WHEN rank = 2 THEN video_id END), -1),
        COALESCE(MAX(CASE WHEN rank = 2 THEN total_score END), 0.0),
        COALESCE(MAX(CASE WHEN rank = 3 THEN video_id END), -1),
        COALESCE(MAX(CASE WHEN rank = 3 THEN total_score END), 0.0),
        NOW()
    FROM (
        SELECT 
            video_id, 
            SUM(7 - EXTRACT(DAY FROM (NOW() - viewed_at))) as total_score,
            ROW_NUMBER() OVER (ORDER BY SUM(7 - EXTRACT(DAY FROM (NOW() - viewed_at))) DESC) as rank
        FROM video_view
        WHERE viewed_at >= NOW() - INTERVAL '7 days'
        GROUP BY video_id
        LIMIT 3
    ) AS ranked_data
    """, nativeQuery = true)
    void runTrendingEtl();

    @Query(value = "SELECT * FROM trending_videos ORDER BY calculated_at DESC LIMIT 1", nativeQuery = true)
    TrendingVideos findTop3();
}
