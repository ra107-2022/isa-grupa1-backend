package grupa1.jutjubic.repository;

import grupa1.jutjubic.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByVideoIdOrderByTimestampAsc(Long videoId);
}
