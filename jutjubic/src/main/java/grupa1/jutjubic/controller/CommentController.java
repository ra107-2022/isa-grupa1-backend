package grupa1.jutjubic.controller;


import grupa1.jutjubic.dto.CommentDto;
import grupa1.jutjubic.repository.CommentRepository;
import grupa1.jutjubic.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import grupa1.jutjubic.model.Comment;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private ICommentService commentService;
    @Autowired
    private CommentRepository commentRepository;

    public static class CommentRequest{
        private String content;

        public String getContent()
        {
            return content;
        }

        public void setContent(String content){
            this.content = content;
        }
    }

    // create
    @PostMapping("/video/{videoId}")
    public ResponseEntity<?> createComment (@PathVariable Long videoId,
                                            @RequestBody CommentRequest request,
                                            Authentication authentication)
    {
        if (authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(401).body("You must be logged in to add a comment.");
        }

        Long userId = Long.parseLong(authentication.getName());
        Comment saved = commentService.createComment(videoId, userId, request.getContent());
        return ResponseEntity.ok(toDto(saved));
    }

    // update
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment (@PathVariable Long commentId,
                                            @RequestBody CommentRequest request,
                                            Authentication authentication)
    {
        if(authentication==null || !authentication.isAuthenticated()){
            return ResponseEntity.status(401).body("You must be logged in to edit a comment.");
        }

        Long userId = Long.parseLong(authentication.getName());
        Comment updated = commentService.updateComment(commentId, userId, request.getContent());

        return  ResponseEntity.ok(toDto(updated));
    }


    //delete
    @PutMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId,
                                           @RequestBody CommentRequest request,
                                           Authentication authentication)
    {
        if(authentication==null || !authentication.isAuthenticated()){
            return ResponseEntity.status(401).body("You must be logged in to delete a comment.");
        }

        Long userId = Long.parseLong(authentication.getName());
        commentService.deleteComment(commentId, userId);

        return ResponseEntity.ok().build();

    }

    private CommentDto toDto(Comment comment){
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
