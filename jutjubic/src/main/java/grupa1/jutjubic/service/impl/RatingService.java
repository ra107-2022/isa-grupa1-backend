package grupa1.jutjubic.service.impl;

import grupa1.jutjubic.dto.RatingsCount;
import grupa1.jutjubic.dto.UserRating;
import grupa1.jutjubic.model.Rating;
import grupa1.jutjubic.model.VideoMetadata;
import grupa1.jutjubic.model.enums.RatingType;
import grupa1.jutjubic.repository.RatingRepository;
import grupa1.jutjubic.service.IActivityService;
import grupa1.jutjubic.service.IRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class RatingService implements IRatingService {
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private VideoMetadataService videoMetadataService;

    @Autowired
    private IActivityService activityService;

    @Autowired
    private UserService userService;

    RatingService() {}

    public Rating findById(Long id)
    {
        return ratingRepository.findById(id);
    }

    public List<Rating> findByVideoId(Long videoId)
    {
        return ratingRepository.findByVideoMetadata_Id(videoId);
    }

    public List<Rating> findByUserId(Long userId)
    {
        return ratingRepository.findByUser_Id(userId);
    }

    public UserRating findByVideoIdAndUserId(Long videoId, Long userId)
    {
        var rating = ratingRepository.findByVideoMetadata_IdAndUserId(videoId, userId);
        return new UserRating(rating.getRatingType());
    }

    public UserRating like(Long videoId, Long userId)
    {
        var existing = ratingRepository.findByVideoMetadata_IdAndUserId(videoId, userId);
        if (existing != null)
        {
            if (existing.getRatingType() == RatingType.LIKE)
            {
                existing.setRatingType(RatingType.NONE);
                activityService.removeLike(userId, videoId);
            }
            else if (existing.getRatingType() == RatingType.DISLIKE)
            {
                existing.setRatingType(RatingType.LIKE);
                activityService.removeDislike(userId, videoId);
            }
            else
            {
                existing.setRatingType(RatingType.LIKE);
            }
            var newRating = ratingRepository.save(existing);
            return new UserRating(newRating.getRatingType());
        }
        VideoMetadata video = videoMetadataService.findById(videoId).orElseThrow(() -> new RuntimeException("Video Not Found"));
        var newRating = ratingRepository.save(new Rating(
                video,
                userService.findById(userId),
                RatingType.LIKE,
                LocalDateTime.now()
        ));
        return  new UserRating(newRating.getRatingType());
    }

    public UserRating dislike(Long videoId, Long userId)
    {
        var existing = ratingRepository.findByVideoMetadata_IdAndUserId(videoId, userId);
        if (existing != null)
        {
            if (existing.getRatingType() == RatingType.LIKE)
            {
                existing.setRatingType(RatingType.DISLIKE);
                activityService.removeLike(userId, videoId);
            }
            else if (existing.getRatingType() == RatingType.DISLIKE)
            {
                existing.setRatingType(RatingType.NONE);
                activityService.removeDislike(userId, videoId);
            }
            else
            {
                existing.setRatingType(RatingType.DISLIKE);
            }
            var newRating = ratingRepository.save(existing);
            return new UserRating(newRating.getRatingType());
        }
        VideoMetadata video = videoMetadataService.findById(videoId).orElseThrow(() -> new RuntimeException("Video Not Found"));
        var newRating = ratingRepository.save(new Rating(
                video,
                userService.findById(userId),
                RatingType.DISLIKE,
                LocalDateTime.now()
        ));
        return  new UserRating(newRating.getRatingType());
    }

    public RatingsCount getCounts(Long videoId)
    {
        RatingsCount ratingsCount = new RatingsCount();
        List<Rating> ratings = ratingRepository.findByVideoMetadata_Id(videoId);
        for (Rating rating : ratings)
        {
            if (rating.getRatingType() == RatingType.LIKE)
            {
                ratingsCount.setLikes(ratingsCount.getLikes() + 1);
            }
            else if (rating.getRatingType() == RatingType.DISLIKE)
            {
                ratingsCount.setDislikes(ratingsCount.getDislikes() + 1);
            }
        }
        return  ratingsCount;
    }
}
