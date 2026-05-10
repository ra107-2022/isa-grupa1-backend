package grupa1.jutjubic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import grupa1.jutjubic.config.RabbitMQConfig;
import grupa1.jutjubic.dto.UploadEventDto;
import grupa1.jutjubic.proto.UploadEventProto;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/benchmark")
public class BenchmarkController {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public BenchmarkController(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/mq")
    public Map<String, Object> benchmark() throws Exception {
        int count = 50;
        long totalJsonSer = 0, totalProtoSer = 0;
        long totalJsonSize = 0, totalProtoSize = 0;

        for (int i = 0; i < count; i++) {
            UploadEventDto event = new UploadEventDto(
                    "Test Video " + i, 1024L * 1024 * (i + 1),
                    "testuser", "video_" + i + ".mp4",
                    System.currentTimeMillis()
            );

            long start = System.nanoTime();
            byte[] jsonBytes = objectMapper.writeValueAsBytes(event);
            totalJsonSer += System.nanoTime() - start;
            totalJsonSize += jsonBytes.length;
            MessageProperties props = new MessageProperties();
            props.setContentType("application/json");
            rabbitTemplate.send(RabbitMQConfig.JSON_QUEUE, new Message(jsonBytes, props));

            start = System.nanoTime();
            byte[] protoBytes = UploadEventProto.UploadEvent.newBuilder()
                    .setTitle(event.getTitle())
                    .setSize(event.getSize())
                    .setAuthor(event.getAuthor())
                    .setFilename(event.getFilename())
                    .setTimestamp(event.getTimestamp())
                    .build().toByteArray();
            totalProtoSer += System.nanoTime() - start;
            totalProtoSize += protoBytes.length;
            props = new MessageProperties();
            props.setContentType("application/x-protobuf");
            rabbitTemplate.send(RabbitMQConfig.PROTO_QUEUE, new Message(protoBytes, props));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("json_avg_serialization_s", totalJsonSer / count / 1_000_000.0);
        result.put("json_avg_size_bytes", totalJsonSize / count);
        result.put("proto_avg_serialization_s", totalProtoSer / count / 1_000_000.0);
        result.put("proto_avg_size_bytes", totalProtoSize / count);
        return result;
    }
}
