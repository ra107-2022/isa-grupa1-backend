package grupa1.jutjubic.repository;

import grupa1.jutjubic.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    Rating findById(Long id);
    List<Rating> findByVideoMetadata_Id(Long videoMetadataId);
    List<Rating> findByUser_Id(Long userId);
    Rating findByVideoMetadata_IdAndUserId(Long videoId, Long userId);
}
