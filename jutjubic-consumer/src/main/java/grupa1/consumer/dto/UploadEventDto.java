package grupa1.consumer.dto;

public class UploadEventDto {
    private String title;
    private Long size;
    private String author;
    private String filename;
    private Long timestamp;

    public UploadEventDto() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Long getSize() { return size; }
    public void setSize(Long size) { this.size = size; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}
