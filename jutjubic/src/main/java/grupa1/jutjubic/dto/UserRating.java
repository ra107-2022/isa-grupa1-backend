package grupa1.jutjubic.dto;

import grupa1.jutjubic.model.enums.RatingType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRating {
    private String ratingType;
    public UserRating(RatingType ratingType) {
        this.ratingType = ratingType.toString();
    }
}
