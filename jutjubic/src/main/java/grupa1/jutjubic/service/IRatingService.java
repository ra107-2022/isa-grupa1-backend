package grupa1.jutjubic.service;

import grupa1.jutjubic.dto.RatingsCount;
import grupa1.jutjubic.dto.UserRating;
import grupa1.jutjubic.model.Rating;

import java.util.List;

public interface IRatingService {
    Rating findById(Long id);
    List<Rating> findByVideoId(Long videoId);
    List<Rating> findByUserId(Long userId);
    UserRating findByVideoIdAndUserId(Long videoId, Long userId);
    UserRating like(Long videoId, Long userId);
    UserRating dislike(Long videoId, Long userId);
    RatingsCount getCounts(Long videoId);
}
