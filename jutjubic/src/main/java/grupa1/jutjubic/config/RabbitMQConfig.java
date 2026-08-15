package grupa1.jutjubic.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.MessageConverter;


@Configuration
public class RabbitMQConfig {
    public static final String TRANSCODING_EXCHANGE = "video.transcoding.exchange";

    public static final String VIDEO_QUEUE = "video.transcoding.queue";
    public static final String VIDEO_LOG_QUEUE = "video.transcoding.log.queue";
    public static final String JSON_QUEUE = "upload.event.json";
    public static final String PROTO_QUEUE = "upload.event.proto";

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(TRANSCODING_EXCHANGE);
    }

    @Bean
    public Queue videoQueue(){
        return new Queue(VIDEO_QUEUE, true);
    }

    @Bean
    public Queue videoLogQueue(){
        return new Queue(VIDEO_LOG_QUEUE, true);
    }

    @Bean
    public Binding bindVideoQueue(Queue videoQueue, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(videoQueue).to(fanoutExchange);
    }

    @Bean
    public Binding bindVideoLogQueue(Queue videoLogQueue, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(videoLogQueue).to(fanoutExchange);
    }

    // RbbitMQ -  posta
    // Queue - ladica sa zadacima
    // VIDEO_QUEUE - ladica

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

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
