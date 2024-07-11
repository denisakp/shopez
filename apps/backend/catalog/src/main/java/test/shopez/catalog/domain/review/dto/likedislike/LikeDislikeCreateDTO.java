package test.shopez.catalog.domain.review.dto.likedislike;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LikeDislikeCreateDTO {
    @NotBlank
    private String authorId;

    @NotNull
    private boolean like;
}
