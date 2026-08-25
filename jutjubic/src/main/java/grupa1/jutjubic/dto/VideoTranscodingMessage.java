package grupa1.jutjubic.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class VideoTranscodingMessage {

    private String inputPath;
    private String outputPath;

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }
}