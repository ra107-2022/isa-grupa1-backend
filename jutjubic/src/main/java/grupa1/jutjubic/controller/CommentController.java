package grupa1.jutjubic.controller;

import grupa1.jutjubic.dto.CommentDto;
import grupa1.jutjubic.dto.CommentPageDto;
import grupa1.jutjubic.model.Comment;
import grupa1.jutjubic.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    public static class CommentRequest {
        private String content;

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    // GET komentara po videu (paginacija)
    @GetMapping("/video/{videoId}")
    public ResponseEntity<CommentPageDto> getComments(
            @PathVariable Long videoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<Comment> commentPage = commentService.getCommentsByVideoId(videoId, page, size);

        List<CommentDto> dtos = commentPage.getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        CommentPageDto pageDto = new CommentPageDto(
                dtos,
                commentPage.getNumber(),
                commentPage.getSize(),
                commentPage.getTotalElements(),
                commentPage.getTotalPages()
        );

        return ResponseEntity.ok(pageDto);
    }

    // Kreiranje komentara
    @PostMapping("/video/{videoId}")
    public ResponseEntity<?> createComment(
            @PathVariable Long videoId,
            @RequestBody CommentRequest request,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body("You must be logged in to add a comment.");
        }

        Long userId = Long.parseLong(authentication.getName());
        Comment saved = commentService.createComment(videoId, userId, request.getContent());

        return ResponseEntity.ok(toDto(saved));
    }

    // Izmena komentara
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequest request,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body("You must be logged in to edit a comment.");
        }

        Long userId = Long.parseLong(authentication.getName());
        Comment updated = commentService.updateComment(commentId, userId, request.getContent());

        return ResponseEntity.ok(toDto(updated));
    }

    // Brisanje komentara
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body("You must be logged in to delete a comment.");
        }

        Long userId = Long.parseLong(authentication.getName());
        commentService.deleteComment(commentId, userId);

        return ResponseEntity.ok().build();
    }

    // mapiranje Comment -> CommentDto
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