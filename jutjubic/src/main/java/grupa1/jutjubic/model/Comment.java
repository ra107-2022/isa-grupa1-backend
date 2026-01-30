package grupa1.jutjubic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "comments",
        indexes = {
                @Index(name = "idx_comment_video_created", columnList = "video_id, created_at"),
                @Index(name = "idx_comment_author_created", columnList = "author_id, created_at")
        }
)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Autor komentara
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Video na koji se komentar odnosi
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "video_id", nullable = false)
    private VideoMetadata video;

    // Tekst komentara
    @Column(nullable = false, length = 1000)
    private String content;

    // Datum kreiranja
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Datum poslednje izmene
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Comment() {}

    public Comment(User author, VideoMetadata video, String content) {
        this.author = author;
        this.video = video;
        this.content = content;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}