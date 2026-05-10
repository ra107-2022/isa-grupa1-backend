package grupa1.jutjubic.service.impl;

import grupa1.jutjubic.model.VideoMetadata;
import grupa1.jutjubic.repository.VideoMetadataRepository;
import grupa1.jutjubic.service.IThumbnailCompressionService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ThumbnailCompressionService implements IThumbnailCompressionService {

    private final VideoMetadataRepository repository;

    public ThumbnailCompressionService (VideoMetadataRepository repository){
        this.repository = repository;
    }

    @Override
    @Scheduled(cron = "0 0 3 * * *")
    //@Scheduled(cron = "0 * * * * *") //sekunda minuta sat dan mesec danUNedelji
    public void compressOldThumbnails() {
        LocalDateTime limit = LocalDateTime.now().minusMonths(1); // mesec dana
       //LocalDateTime limit = LocalDateTime.now().minusMinutes(2); // 2min za test

        List<VideoMetadata> videos = repository.findByThumbnailCompressedFalseAndUploadDateBefore(limit);

        for(VideoMetadata v : videos){
            try {
                compress(v);
                v.setThumbnailCompressed(true);
                repository.save(v);
            } catch (Exception e){
                System.out.println("Compression failed for " + v.getId());
            }
        }
    }

    private void compress(VideoMetadata v) throws  Exception {

        String inputPath = "uploads/thumbnails/" + v.getThumbnailFileName();
        String outputPath = inputPath.replace(".jpg", "_compressed.jpg");

        File originalFile = new File(inputPath);
        System.out.println("Kompresija pocela za video: " + v.getId());
        System.out.println("Pre: " + originalFile.length());

        Thumbnails.of(new File(inputPath)).size(800,800)
                .outputQuality(0.6)
                .toFile(new File(outputPath));

        File compressedFile = new File(outputPath);

        System.out.println("Posle: " + compressedFile.length());
        System.out.println("GOTOVA KOMPESIJA ");

    }

}
