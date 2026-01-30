package grupa1.jutjubic.controller;

import grupa1.jutjubic.model.enums.ActivityType;
import grupa1.jutjubic.service.IActivityService;
import grupa1.jutjubic.service.IUserService;
import grupa1.jutjubic.service.impl.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class VideoActivityController {
    @Autowired
    private IActivityService activityService;

    @Autowired
    private IUserService userService;

    @PostMapping("/{videoId}/log")
    public ResponseEntity<Void> log(@PathVariable Long videoId,
                                    @RequestParam ActivityType type,
                                    @RequestParam double lon,
                                    @RequestParam double lat,
                                    Principal user) {
        if (user != null)
        {
            Long userId = userService.findByUsername(user.getName()).getId();
            activityService.logActivity(videoId, userId, type, lon, lat);
        }
        else
        {
            activityService.logActivity(videoId, null, type, lon, lat);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/trending/local")
    public List<Long> getTrending(@RequestParam double lon,
                                  @RequestParam double lat,
                                  @RequestParam(defaultValue = "50000") double radius, // 50km
                                  @RequestParam(defaultValue = "10") int limit) {
        return activityService.getTrendingVideos(lon, lat, radius, limit);
    }
}
