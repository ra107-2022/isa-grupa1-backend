package grupa1.jutjubic.controller;

import grupa1.jutjubic.dto.CommentDto;
import grupa1.jutjubic.model.Comment;
import grupa1.jutjubic.model.User;
import grupa1.jutjubic.service.ICommentService;
import grupa1.jutjubic.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IUserService userService;

    public static class CommentRequest {
        private String content;

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    // GET comments paginated
    @GetMapping("/video/{videoId}")
    public ResponseEntity<?> getCommentsByVideo(@PathVariable Long videoId,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "20") int size)
    {
        Page<Comment> commentsPage = commentService.getCommentsByVideoId(videoId, page, size);
        Page<CommentDto> dtosPage = commentsPage.map(this::toDto);
        return ResponseEntity.ok(dtosPage);
    }

    // CREATE comment
    @PostMapping("/video/{videoId}")
    public ResponseEntity<?> createComment(@PathVariable Long videoId,
                                           @RequestBody CommentRequest request,
                                           Authentication authentication)
    {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("You must be logged in to add a comment.");
        }

        User loggedUser = userService.findByUsername(authentication.getName());
        Comment saved = commentService.createComment(videoId, loggedUser.getId(), request.getContent());
        return ResponseEntity.ok(toDto(saved));
    }

    // UPDATE comment
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId,
                                           @RequestBody CommentRequest request,
                                           Authentication authentication)
    {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("You must be logged in to edit a comment.");
        }

        User loggedUser = userService.findByUsername(authentication.getName());
        Comment updated = commentService.updateComment(commentId, loggedUser.getId(), request.getContent());
        return ResponseEntity.ok(toDto(updated));
    }

    // DELETE comment
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId,
                                           Authentication authentication)
    {
        if (authentication == null || !authentication.isAuthenticated())
        {
            return ResponseEntity.status(401).body("You must be logged in to delete a comment.");
        }

        User loggedUser = userService.findByUsername(authentication.getName());
        commentService.deleteComment(commentId, loggedUser.getId());
        return ResponseEntity.ok().build();
    }

    private CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getAuthor().getId(),
                comment.getAuthor().getUsername(),
                comment.getVideo().getId(),
                comment.getVideo().getVideoTitle(),
                comment.getVideo().getUser().getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}