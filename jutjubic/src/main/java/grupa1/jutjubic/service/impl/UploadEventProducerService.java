package grupa1.jutjubic.service.impl;

import grupa1.jutjubic.service.IUploadEventProducerService;

import com.fasterxml.jackson.databind.ObjectMapper;
import grupa1.jutjubic.config.RabbitMQConfig;
import grupa1.jutjubic.dto.UploadEventDto;
import grupa1.jutjubic.proto.UploadEventProto;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class UploadEventProducerService implements IUploadEventProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public UploadEventProducerService(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendJsonEvent(UploadEventDto event) {
        try {
            byte[] body = objectMapper.writeValueAsBytes(event);
            MessageProperties props = new MessageProperties();
            props.setContentType("application/json");
            rabbitTemplate.send(RabbitMQConfig.JSON_QUEUE, new Message(body, props));
        } catch (Exception e) {
            System.err.println("Failed to send JSON event: " + e.getMessage());
        }
    }

    public void sendProtoEvent(UploadEventDto event) {
        try {
            UploadEventProto.UploadEvent protoEvent = UploadEventProto.UploadEvent.newBuilder()
                    .setTitle(event.getTitle())
                    .setSize(event.getSize())
                    .setAuthor(event.getAuthor())
                    .setFilename(event.getFilename())
                    .setTimestamp(event.getTimestamp())
                    .build();
            byte[] body = protoEvent.toByteArray();
            MessageProperties props = new MessageProperties();
            props.setContentType("application/x-protobuf");
            rabbitTemplate.send(RabbitMQConfig.PROTO_QUEUE, new Message(body, props));
        } catch (Exception e) {
            System.err.println("Failed to send Protobuf event: " + e.getMessage());
        }
    }
}
