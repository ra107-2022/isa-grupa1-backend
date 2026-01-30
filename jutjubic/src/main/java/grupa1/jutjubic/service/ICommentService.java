package grupa1.jutjubic.service;

import grupa1.jutjubic.model.Comment;
import org.springframework.data.domain.Page;

public interface ICommentService {

    Page<Comment> getCommentsByVideoId(Long videoId, int page, int size);

    Comment createComment(Long videoId, Long userId, String content);

    Comment updateComment(Long commentId, Long userId, String newContent);

    void deleteComment(Long commentId, Long userId);
}
