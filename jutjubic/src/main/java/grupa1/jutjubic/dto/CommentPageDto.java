package grupa1.jutjubic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CommentPageDto {
    private List<CommentDto> comments;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
