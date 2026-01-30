package grupa1.jutjubic.service.impl;

import grupa1.jutjubic.dto.ViewData;
import grupa1.jutjubic.model.User;
import grupa1.jutjubic.model.VideoMetadata;
import grupa1.jutjubic.model.VideoView;
import grupa1.jutjubic.repository.UserRepository;
import grupa1.jutjubic.repository.VideoMetadataRepository;
import grupa1.jutjubic.repository.VideoViewRepository;
import grupa1.jutjubic.service.IViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ViewService implements IViewService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoMetadataRepository videoRepository;

    @Autowired
    private VideoViewRepository viewRepository;

    @Override
    public Optional<Long> getViewCount(Long videoId) {
        long ret = (long) viewRepository.findAllByVideoMetadata_Id(videoId).size();
        if (ret >= 0) {
            return Optional.of(ret);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findUsersWhoViewedVideo(Long videoId) {
        return viewRepository
                .findAllByVideoMetadata_Id(videoId)
                .stream()
                .map(VideoView::getUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<VideoMetadata> findVideosViewedByUser(Long viewerId) {
        return viewRepository
                .findAllByUser_Id(viewerId)
                .stream()
                .map(VideoView::getVideoMetadata)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<VideoView> save(ViewData viewData) {
        Optional<VideoMetadata> videoOpt = videoRepository.findById(viewData.getVideoId());
        if (videoOpt.isEmpty()) { return Optional.empty(); }
        Optional<User> userOpt = userRepository.findById(viewData.getViewerId());
        return userOpt.map(user -> viewRepository.save(new VideoView(videoOpt.get(), user, LocalDateTime.now())));
    }
}
