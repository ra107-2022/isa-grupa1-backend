package grupa1.jutjubic.service.impl;

import grupa1.jutjubic.config.RabbitMQConfig;
import grupa1.jutjubic.dto.VideoTranscodingMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class VideoConsumer2 {

    @RabbitListener (queues = RabbitMQConfig.VIDEO_LOG_QUEUE) // kad stigne nova poruka u video-transcoding-queue
    public void consume(VideoTranscodingMessage message){

        System.out.println("Consumer2 dobio poruku (status log)");

        System.out.println("Input: " + message.getInputPath());
        System.out.println("Output: " + message.getOutputPath());

        System.out.println("STATUS: video poslat na obradu");
    }

    // Consumer1 prima poruku iz RabbitMQ i pokrece FFmpeg transcoding videa
    // Consumer2 prima istu poruku i samo loguje/obradjuje status (bez video obrade)

    // RabbitMQ radi distribuciju poruka (load balancing), pa ako oba consumer-a rade FFmpeg
    // isti video bi mogao biti obradjen 2 puta
    // ili bi se logika duplirala
    // po specifikaciji se trazi da 1 consumer radi tesku obradu (transcoding),
    // a drugi samo prati/registruje status da bi sistem imao minimalno 2 potrosaca bez duplog procesiranja
}
