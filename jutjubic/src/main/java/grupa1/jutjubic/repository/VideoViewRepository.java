package grupa1.jutjubic.repository;

import grupa1.jutjubic.model.User;
import grupa1.jutjubic.model.VideoMetadata;
import grupa1.jutjubic.model.VideoView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoViewRepository extends JpaRepository<VideoView, Long> {
    List<VideoView> findAllByUser_Id(Long userId);
    List<VideoView> findAllByVideoMetadata_Id(Long videoMetadataId);
}
