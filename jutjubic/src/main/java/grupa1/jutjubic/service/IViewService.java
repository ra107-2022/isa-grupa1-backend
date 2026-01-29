package grupa1.jutjubic.service;

import grupa1.jutjubic.dto.ViewData;
import grupa1.jutjubic.model.User;
import grupa1.jutjubic.model.VideoMetadata;
import grupa1.jutjubic.model.VideoView;

import java.util.List;
import java.util.Optional;

public interface IViewService {
    Optional<Long> getViewCount(Long videoId);
    List<User> findUsersWhoViewedVideo(Long videoId);
    List<VideoMetadata> findVideosViewedByUser(Long viewerId);
    Optional<VideoView> save(ViewData viewData);
}
