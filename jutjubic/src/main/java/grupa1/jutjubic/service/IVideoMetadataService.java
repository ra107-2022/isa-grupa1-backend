package grupa1.jutjubic.service;

import grupa1.jutjubic.dto.UploadRequest;
import grupa1.jutjubic.model.VideoMetadata;
import org.springframework.core.io.Resource;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface IVideoMetadataService {
    Optional<VideoMetadata> findById(Long id);
    Optional<VideoMetadata> findByEmailAndTitle(String email, String title);
    Optional<VideoMetadata> findByUsernameAndTitle(String username, String title);
    List<VideoMetadata> findAll();
    Optional<VideoMetadata> save(UploadRequest uploadRequest);
    Optional<Resource> loadVideoAsResource(Long id);
    Optional<Resource> loadThumbnailAsResource(Long id);
    Path getFilePath(VideoMetadata metadata, boolean forVideo);
    List<Long> getPage(Long start, Long count);
}
