package grupa1.jutjubic.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ChatMessageDto {
    private String username;
    private String content;
    private LocalDateTime timestamp;

    public ChatMessageDto() { }

    public ChatMessageDto(String username, String content, LocalDateTime timestamp) {
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
    }
}
