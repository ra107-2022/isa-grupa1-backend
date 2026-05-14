package grupa1.jutjubic.service.impl;

import grupa1.jutjubic.config.RabbitMQConfig;
import grupa1.jutjubic.dto.VideoTranscodingMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class VideoProducer {
    private final RabbitTemplate rabbitTemplate;

    public VideoProducer(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendVideoForTranscoding(VideoTranscodingMessage msg) {
        rabbitTemplate.convertAndSend("video-transcoding-queue", msg);
        System.out.println("Poslat video u queue: " + msg.getInputPath());
    }
}
