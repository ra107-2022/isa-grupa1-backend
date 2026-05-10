package grupa1.jutjubic.repository;

import grupa1.jutjubic.model.VideoMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VideoMetadataRepository extends JpaRepository<VideoMetadata, Long> {
    Optional<VideoMetadata> findByUser_IdAndVideoTitle(Long userId, String videoTitle);
    List<VideoMetadata> findByThumbnailCompressedFalseAndUploadDateBefore(LocalDateTime dateTime);
}
