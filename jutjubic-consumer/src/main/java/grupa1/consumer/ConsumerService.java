package grupa1.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import grupa1.consumer.dto.UploadEventDto;
import grupa1.consumer.proto.UploadEventProto;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "upload.event.json")
    public void receiveJsonEvent(Message message) {
        try {
            long start = System.nanoTime();
            UploadEventDto event = objectMapper.readValue(message.getBody(), UploadEventDto.class);
            long end = System.nanoTime();
            System.out.println("[JSON] Received: " + event.getTitle() +
                    " | Size: " + message.getBody().length + " bytes" +
                    " | Deserialization: " + (end - start) + " ns");
        } catch (Exception e) {
            System.err.println("Failed to deserialize JSON: " + e.getMessage());
        }
    }

    @RabbitListener(queues = "upload.event.proto")
    public void receiveProtoEvent(Message message) {
        try {
            long start = System.nanoTime();
            UploadEventProto.UploadEvent event = UploadEventProto.UploadEvent.parseFrom(message.getBody());
            long end = System.nanoTime();
            System.out.println("[PROTO] Received: " + event.getTitle() +
                    " | Size: " + message.getBody().length + " bytes" +
                    " | Deserialization: " + (end - start) + " ns");
        } catch (Exception e) {
            System.err.println("Failed to deserialize Protobuf: " + e.getMessage());
        }
    }
}
