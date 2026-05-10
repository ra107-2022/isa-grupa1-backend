package grupa1.jutjubic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(optional = false)
    @JoinColumn(name = "video_id")
    private VideoMetadata video;

    public ChatMessage(String username, String content, VideoMetadata video) {
        this.username = username;
        this.content = content;
        this.video = video;
        this.timestamp = LocalDateTime.now();
    }
}
