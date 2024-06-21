package test.shopez.catalog.domain.review.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeDislike {
    private String authorId;
    private boolean like;
}
