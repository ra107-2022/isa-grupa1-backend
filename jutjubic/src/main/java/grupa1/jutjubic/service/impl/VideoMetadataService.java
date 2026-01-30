package grupa1.jutjubic.service.impl;

import grupa1.jutjubic.dto.UploadRequest;
import grupa1.jutjubic.model.User;
import grupa1.jutjubic.model.VideoMetadata;
import grupa1.jutjubic.repository.UserRepository;
import grupa1.jutjubic.repository.VideoMetadataRepository;
import grupa1.jutjubic.service.IVideoMetadataService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VideoMetadataService implements IVideoMetadataService {
    @Value("${video.upload-dir:uploads/videos}")
    private String videoDir;

    @Value("${video.upload-dir:uploads/thumbnails}")
    private String thumbnailDir;

    @Autowired
    private VideoMetadataRepository videoMetadataRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<VideoMetadata> findById(Long id) { return videoMetadataRepository.findById(id); }

    @Override
    public Optional<VideoMetadata> findByEmailAndTitle(String email, String title) {
        User user;
        try {
            user = userRepository.findByEmail(email);
        } catch (Exception e) {
            return Optional.empty();
        }
        return videoMetadataRepository.findByUser_IdAndVideoTitle(user.getId(), title);
    }

    @Override
    public Optional<VideoMetadata> findByUsernameAndTitle(String username, String title) {
        User user;
        try {
            user = userRepository.findByUsername(username);
        } catch(Exception e) {
            return Optional.empty();
        }
        return videoMetadataRepository.findByUser_IdAndVideoTitle(user.getId(), title);
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(videoDir));
            Files.createDirectories(Paths.get(thumbnailDir));
        } catch (Exception e) {
            throw new RuntimeException("Could not create directories to store videos and thumbnails!");
        }
    }

    @Override
    public List<VideoMetadata> findAll() {
        return videoMetadataRepository.findAll();
    }

    @Override
    public Optional<VideoMetadata> save(UploadRequest uploadRequest) {
         Optional<VideoMetadata> video = videoMetadataRepository.findByUser_IdAndVideoTitle(uploadRequest.getOwnerId(), uploadRequest.getTitle()) ;
         if (video.isPresent()) { return Optional.empty(); }

         Optional<User> user_opt = userRepository.findById(uploadRequest.getOwnerId());
         if (user_opt.isEmpty()) { return Optional.empty(); }

         String videoFileName = UUID.randomUUID() + "_" + uploadRequest
                 .getVideo()
                 .getName()
                 .replaceAll("[^a-zA-Z0-9.\\-]", "_");
         videoFileName += ".mp4";
         Path videoPath = Paths.get(videoDir).resolve(videoFileName);
         try {
             if (Files.exists(videoPath)) {
                 return Optional.empty();
             }
             Files.copy(uploadRequest.getVideo().getInputStream(), videoPath, StandardCopyOption.REPLACE_EXISTING);
         } catch (Exception e) {
             return Optional.empty();
         }

         String thumbnailFileName = UUID.randomUUID() + "_" + uploadRequest
                .getThumbnail()
                .getName()
                .replaceAll("[^a-zA-Z0-9.\\-]", "_");
         thumbnailFileName += ".jpg";
         Path thumbnailPath = Paths.get(thumbnailDir).resolve(thumbnailFileName);
         try {
             if (Files.exists(thumbnailPath)) {
                 Files.delete(videoPath);
                 return Optional.empty();
             }
             Files.copy(uploadRequest.getThumbnail().getInputStream(), thumbnailPath, StandardCopyOption.REPLACE_EXISTING);
         } catch (Exception e1) {
             try {
                 Files.delete(videoPath);
             } catch (Exception e2) { return Optional.empty(); }
             return Optional.empty();
         }

         String tags = uploadRequest
                 .getTags()
                 .stream()
                 .map((tag) -> tag.replaceAll("\\|", "_"))
                 .reduce("", (acc, tag) -> acc + "|" + tag);

         return Optional.of(videoMetadataRepository.save(new VideoMetadata(
                  user_opt.get(),
                  LocalDateTime.now(),
                  uploadRequest.getTitle(),
                  uploadRequest.getDescription(),
                  tags,
                  videoFileName,
                  uploadRequest.getVideo().getSize(),
                  uploadRequest.getVideo().getName(),
                  thumbnailFileName,
                  uploadRequest.getThumbnail().getSize(),
                  uploadRequest.getThumbnail().getName(),
                  uploadRequest.getLat(),
                  uploadRequest.getLon()
         )));
    }

    @Override
    public Optional<Resource> loadVideoAsResource(Long id) {
        Optional<VideoMetadata> metadataOpt = videoMetadataRepository.findById(id);
        if (metadataOpt.isEmpty()) { return Optional.empty(); }
        VideoMetadata metadata = metadataOpt.get();

        Path filePath = getFilePath(metadata, true);
        try {
            Resource res = new UrlResource(filePath.toUri());
            if (res.isReadable() && res.exists()) {
                return Optional.of(res);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Resource> loadThumbnailAsResource(Long id) {
        System.out.println("Looking for: " + id.toString());
        Optional<VideoMetadata> metadataOpt = videoMetadataRepository.findById(id);
        if (metadataOpt.isEmpty()) { return Optional.empty(); }
        VideoMetadata metadata = metadataOpt.get();
        System.out.println("Found: " + metadata.toString());

        Path filePath = getFilePath(metadata, false);
        System.out.println("FilePath: " + filePath);
        try {
            Resource res = new UrlResource(filePath.toUri());
            if (res.isReadable() && res.exists()) {
                System.out.println(res.toString());
                return Optional.of(res);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Path getFilePath(VideoMetadata metadata, boolean forVideo) {
        return Paths
                .get(forVideo ? videoDir : thumbnailDir)
                .resolve(forVideo ? metadata.getVideoFileName() : metadata.getThumbnailFileName());
    }

    @Override
    public List<Long> getPage(Long start, Long count) {
        return videoMetadataRepository
                .findAll()
                .stream()
                .map(VideoMetadata::getId)
                .skip(start * count)
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<VideoMetadata> addView(Long metadataId) {
        Optional<VideoMetadata> metadataOpt = videoMetadataRepository.findById(metadataId);
        if (metadataOpt.isEmpty()) {
            return Optional.empty();
        }
        VideoMetadata metadata = metadataOpt.get();
        metadata.setGuestViews(metadata.getGuestViews() + 1);
        return Optional.of(videoMetadataRepository.save(metadata));
    }
}
