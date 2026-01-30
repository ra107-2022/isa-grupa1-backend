package grupa1.jutjubic.controller;

import grupa1.jutjubic.dto.UploadRequest;
import grupa1.jutjubic.dto.VideoInfo;
import grupa1.jutjubic.model.VideoMetadata;
import grupa1.jutjubic.model.VideoView;
import grupa1.jutjubic.service.IVideoMetadataService;
import grupa1.jutjubic.service.IViewService;
import grupa1.jutjubic.service.impl.ActivityService;
import grupa1.jutjubic.service.impl.UserService;
import grupa1.jutjubic.service.impl.VideoMetadataService;
import grupa1.jutjubic.service.impl.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoMetadataService videoService;

    @Autowired
    private UserService userService;

    @Autowired
    private ViewService viewService;

    @Autowired
    private ActivityService activityService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<VideoMetadata> uploadVideo(
            Principal user,
            @RequestParam("title") String title,
            @RequestParam("video") MultipartFile videoFile,
            @RequestParam("thumbnail") MultipartFile thumbnailFile,
            @RequestParam("description") String description,
            @RequestParam(name = "latitude", required = false) Long lat,
            @RequestParam(name = "longitude", required = false) Long lon,
            @RequestParam("tags") List<String> tags
        ) {
        final Long ownerId = userService.findByUsername(user.getName()).getId();
        return videoService
                .save(new UploadRequest(ownerId , title, description, tags, videoFile, thumbnailFile, lat, lon))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }

    @GetMapping("/{id}/thumbnail")
    public ResponseEntity<Resource> getThumbnail(
            @PathVariable Long id
        ) {
        Optional<Resource> opt = videoService.loadThumbnailAsResource(id);
        return opt
                .map(value -> ResponseEntity
                        .ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(value))
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());
    }

    @GetMapping("/{id}/video")
    public ResponseEntity<Resource> getVideo(
            @PathVariable Long id
        ) {
        Optional<Resource> opt = videoService.loadVideoAsResource(id);
        return opt
                .map(value -> ResponseEntity
                        .ok()
                        .contentType(MediaType.parseMediaType("video/mp4"))
                        .body(value))
                .orElseGet(() -> ResponseEntity
                        .notFound()
                        .build());
    }

    @GetMapping("/{id}/video_info")
    public ResponseEntity<VideoInfo> getVideoInfo(
            @PathVariable Long id
        ) {
        return viewService.getViewCount(id)
                .map(viewCount -> videoService
                        .findById(id)
                        .map(metadata -> ResponseEntity
                                .ok()
                                .body(new VideoInfo(metadata.getVideoTitle(), viewCount, metadata.getUser().getUsername())))
                        .orElseGet(() -> ResponseEntity
                                .notFound()
                                .build()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/get_page")
    public ResponseEntity<List<Long>> getPage(
            @RequestParam("start") Long start,
            @RequestParam("count") Long count
        ) {
        return ResponseEntity.ok().body(videoService.getPage(start, count));
    }
}
