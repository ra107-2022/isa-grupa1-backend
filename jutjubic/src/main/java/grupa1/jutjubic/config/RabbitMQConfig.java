package grupa1.jutjubic.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

@Configuration
public class RabbitMQConfig {

    public static final String JSON_QUEUE = "upload.event.json";
    public static final String PROTO_QUEUE = "upload.event.proto";

    @Bean
    public Queue jsonQueue() {
        return new Queue(JSON_QUEUE, true);
    }

    @Bean
    public Queue protoQueue() {
        return new Queue(PROTO_QUEUE, true);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
