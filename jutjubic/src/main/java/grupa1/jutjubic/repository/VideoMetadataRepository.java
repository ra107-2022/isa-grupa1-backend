package grupa1.jutjubic.repository;

import grupa1.jutjubic.model.VideoMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoMetadataRepository extends JpaRepository<VideoMetadata, Long> {
    Optional<VideoMetadata> findByOwnerIdAndVideoTitle(Long ownerId, String videoTitle);
}
