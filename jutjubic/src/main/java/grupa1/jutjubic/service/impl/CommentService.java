package grupa1.jutjubic.service.impl;

import grupa1.jutjubic.model.Comment;
import grupa1.jutjubic.model.User;
import grupa1.jutjubic.model.VideoMetadata;
import grupa1.jutjubic.repository.CommentRepository;
import grupa1.jutjubic.repository.UserRepository;
import grupa1.jutjubic.repository.VideoMetadataRepository;
import grupa1.jutjubic.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService implements ICommentService {

    private static final int MAX_COMMENTS_PER_HOUR = 60;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoMetadataRepository videoRepository;

    @Cacheable(
            value = "videoComments",
            key = "#videoId + '_' + #page + '_' + #size"
    )

    // najnoviji komentari prvi
    @Override
    public Page<Comment> getCommentsByVideoId(Long videoId, int page, int size) {

        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return commentRepository.findByVideoIdOrderByCreatedAtDesc(
                videoId,
                pageRequest
        );
    }

    @CacheEvict(value = "videoComments", allEntries = true)
    //  Kreiranje komentara -- rate-limit (60 / sat)
    @Override
    public Comment createComment(Long videoId, Long userId, String content) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        VideoMetadata video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);

        long commentCount =
                commentRepository.countByAuthorIdAndCreatedAtAfter(
                        user.getId(),
                        oneHourAgo
                );

        if (commentCount >= MAX_COMMENTS_PER_HOUR) {
            throw new RuntimeException(
                    "You have reached the limit of 60 comments per hour."
            );
        }

        Comment comment = new Comment(user, video, content);
        return commentRepository.save(comment);
    }

    @CacheEvict(value = "videoComments", allEntries = true)
    // edit komentara --  samo autor komentara
    @Override
    public Comment updateComment(Long commentId, Long userId, String newContent) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("You are not the author of this comment");
        }

        comment.setContent(newContent);
        return commentRepository.save(comment);
    }

    @CacheEvict(value = "videoComments", allEntries = true)
    // brisanje komentara -- samo autor komentara
    @Override
    public void deleteComment(Long commentId, Long userId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("You are not the author of this comment");
        }

        commentRepository.delete(comment);
    }
}