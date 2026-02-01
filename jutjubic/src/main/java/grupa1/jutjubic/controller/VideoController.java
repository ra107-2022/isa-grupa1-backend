package grupa1.jutjubic.controller;

import grupa1.jutjubic.dto.AllOfVideoInfo;
import grupa1.jutjubic.dto.UploadRequest;
import grupa1.jutjubic.dto.VideoInfo;
import grupa1.jutjubic.dto.ViewData;
import grupa1.jutjubic.model.User;
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
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
            @RequestParam(name = "latitude", required = false) Double lat,
            @RequestParam(name = "longitude", required = false) Double lon,
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
    public ResponseEntity<ResourceRegion> getVideo(
            @PathVariable Long id,
            @RequestHeader HttpHeaders headers
            ) {
        Optional<Resource> opt = videoService.loadVideoAsResource(id);
        if (opt.isEmpty()) { return ResponseEntity.notFound().build(); }

        Resource video = opt.get();
        long contentLen;
        try {
            contentLen = video.contentLength();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.notFound().build();
        }

        HttpRange range = headers.getRange().stream().findFirst().orElse(null);
        if (range != null) {
            long start = range.getRangeStart(contentLen);
            long end = range.getRangeEnd(contentLen);
            long rangeLen = Math.min(1024 * 1024, end - start + 1);

            ResourceRegion res = new ResourceRegion(video, start, rangeLen);
            ResponseEntity<ResourceRegion> ret = ResponseEntity
                    .status(HttpStatus.PARTIAL_CONTENT)
                    .contentType(MediaType.parseMediaType("video/mp4"))
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .header(
                            HttpHeaders.CONTENT_RANGE,
                            "bytes " + start + "-" + (start + rangeLen - 1) + "/" + contentLen)
                    .contentLength(contentLen)
                    .body(res);
            System.out.println(ret.toString());
            return ret;
        } else {
            ResourceRegion res = new ResourceRegion(video, 0, contentLen);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType("video/mp4"))
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .contentLength(contentLen)
                    .body(res);
        }
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
                                .body(new VideoInfo(metadata.getVideoTitle(), viewCount + metadata.getGuestViews(), metadata.getUser().getUsername())))
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

    @GetMapping("/{id}/all_video_info")
    public ResponseEntity<AllOfVideoInfo> getAllOfVideoInfo(
            @PathVariable Long id
        ) {
        Optional<Long> viewOpt = viewService.getViewCount(id);
        if (viewOpt.isEmpty()) { return ResponseEntity.notFound().build(); }

        Optional<VideoMetadata> metadataOpt = videoService.findById(id);
        if (metadataOpt.isEmpty()) { return ResponseEntity.notFound().build(); }

        VideoMetadata metadata = metadataOpt.get();
        String tagStr = metadata.getTags();

        List<String> tags = new ArrayList<String>();
        for (int i = 0; i < tagStr.length(); ++i) {
            if (tagStr.charAt(i) == '|') {
                tags.add("");
            } else {
                tags.set(tags.size() - 1, tags.getLast() + tagStr.charAt(i));
            }
        }
        tags.removeIf(String::isEmpty);

        return ResponseEntity
                .ok()
                .body(new AllOfVideoInfo(
                    metadata.getUser().getId(),
                    metadata.getUser().getUsername(),
                    metadata.getVideoTitle(),
                    metadata.getDescription(),
                    tags,
                    metadata.getUploadDate(),
                    viewOpt.get() + metadata.getGuestViews(),
                    0L,
                    0L
                ));
    }

    @PutMapping("/{id}/view_by_user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> addViewByUser(
            Principal user,
            @PathVariable Long id
        ) {
        Long userId;
        try {
            userId = userService.findByUsername(user.getName()).getId();
        } catch (Exception e) {
            System.out.println("here1");
            return ResponseEntity.notFound().build();
        }
        if (videoService.findById(id).isEmpty()) {
            System.out.println("here2");
            return ResponseEntity.notFound().build();
        }
        return viewService
                .save(new ViewData(id, userId))
                .map(view -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.internalServerError().build());
    }

    @PutMapping("/{id}/view")
    public ResponseEntity<Object> addView(
            @PathVariable Long id
        ) {
        System.out.println("should add for " + id.toString());
        return videoService
                .addView(id)
                .map(view -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.internalServerError().build());
    }
}
