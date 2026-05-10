package grupa1.jutjubic.controller;

import grupa1.jutjubic.dto.ChatMessageDto;
import grupa1.jutjubic.model.ChatMessage;
import grupa1.jutjubic.model.VideoMetadata;
import grupa1.jutjubic.repository.ChatMessageRepository;
import grupa1.jutjubic.repository.VideoMetadataRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;
    private final VideoMetadataRepository videoMetadataRepository;

    public ChatController(ChatMessageRepository chatMessageRepository, VideoMetadataRepository videoMetadataRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.videoMetadataRepository = videoMetadataRepository;
    }

    @MessageMapping("/chat/{videoId}")
    @SendTo("/topic/chat/{videoId}")
    public ChatMessageDto sendMessage(@DestinationVariable Long videoId, ChatMessageDto message) {
        VideoMetadata video = videoMetadataRepository.findById(videoId).orElseThrow();
        ChatMessage entity = new ChatMessage(message.getUsername(), message.getContent(), video);
        chatMessageRepository.save(entity);
        message.setTimestamp(entity.getTimestamp());
        return message;
    }

    @GetMapping("/api/chat/{videoId}")
    public List<ChatMessageDto> getMessages(@PathVariable Long videoId) {
        return chatMessageRepository.findByVideoIdOrderByTimestampAsc(videoId)
                .stream()
                .map(m -> new ChatMessageDto(m.getUsername(), m.getContent(), m.getTimestamp()))
                .collect(Collectors.toList());
    }
}
