package grupa1.jutjubic.service.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
import grupa1.jutjubic.dto.VideoTranscodingMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class VideoConsumer {

    // consumer slusa queue i ceka nove poruke
    // rabitmq ce automatski poslati primljenu poruku samo 1 consumeru
    @RabbitListener(queues = "video-transcoding-queue")
    public void consume(VideoTranscodingMessage message){

        String input = message.getInputPath();
        String output = message.getOutputPath();

        System.out.println("Consumer1 pocinje transcoding...");

         try {
            // FFmpeg command: ffmpeg -i input.mp4 -vf scale=1280:720 output.mp4

            ProcessBuilder builder = new ProcessBuilder(
                    "C:\\ffmpeg\\ffmpeg-8.1.1-essentials_build\\bin\\ffmpeg.exe", // pokrece ffmpeg program
                    "-i", input, // input file i putanja do originalnog fajla
                    "-vf",  // video filter
                    "scale=1280:720", // ffmpeg filter -> promeni rezoluciju videa na 1280x720
                    output //novi kompresovani fajl
                     );
            builder.inheritIO();


            Process process = builder.start();
            process.waitFor(); // ceka se da ffmpeg zavrsi
            System.out.println("Consumer 1 zavrsio transcoding." );

        } catch (Exception e) {
            System.out.println("Greska tokom transcoding.");
            e.printStackTrace();
        }
    }
}
