package grupa1.jutjubic.service.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
import grupa1.jutjubic.config.RabbitMQConfig;
import grupa1.jutjubic.dto.VideoTranscodingMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;

@Component
public class VideoConsumer {
    @Value("${ffmpeg.path:ffmpeg}")
    private String ffmpegPath;
    // consumer slusa queue i ceka nove poruke
    // rabitmq ce automatski poslati primljenu poruku samo 1 consumeru
    @RabbitListener(queues = RabbitMQConfig.VIDEO_QUEUE, concurrency = "2-5")
    public void consume(VideoTranscodingMessage message){

        String input = message.getInputPath();
        String output = message.getOutputPath();

        System.out.println("Consumer1 pocinje transcoding...");

        try {
            File outputFile = new File(output);
            File parentDirectory = outputFile.getParentFile();
            if (parentDirectory != null && !parentDirectory.exists()) {
                boolean created = parentDirectory.mkdirs();
                if (created) {
                    System.out.println("Kreiran folder za processed videa: " + parentDirectory.getAbsolutePath());
                }
            }

            ProcessBuilder builder = new ProcessBuilder(
                    ffmpegPath,
                    "-i", input,
                    "-y",
                    "-vf", "scale=1280:720",
                    output
            );
            builder.inheritIO();

            Process process = builder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Consumer 1 uspesno zavrsio transcoding.");
            } else {
                System.err.println("FFmpeg proces je zavrsio sa greskom. Exit code: " + exitCode);
            }

        } catch (Exception e) {
            System.err.println("Greska tokom transcodinga:");
            e.printStackTrace();
        }
    }
}
