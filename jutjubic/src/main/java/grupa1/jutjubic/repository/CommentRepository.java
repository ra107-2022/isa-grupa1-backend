package grupa1.jutjubic.repository;

import grupa1.jutjubic.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByVideoIdOrderByCreatedAtDesc(Long videoId, Pageable pageable);

    long countByAuthorIdAndCreatedAtAfter(Long authorId, LocalDateTime from);
}