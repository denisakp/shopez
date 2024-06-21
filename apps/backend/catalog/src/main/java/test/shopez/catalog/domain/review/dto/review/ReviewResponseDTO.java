package test.shopez.catalog.domain.review.dto.review;

import test.shopez.catalog.domain.review.entities.LikeDislike;

import java.util.Date;
import java.util.List;

public record ReviewResponseDTO(
        String id,
        String author,
        int note,
        String message,
        Date publishedDate,
        long likes,
        long dislikes
) {
    public ReviewResponseDTO(
            String id,
            String author,
            int note,
            String message,
            List<LikeDislike> likeDislikes,
            Date publishedDate
    ) {
        this(id, author, note, message, publishedDate,
                likeDislikes != null ? likeDislikes.stream().filter(LikeDislike::isLike).count() : 0,
                likeDislikes != null ? likeDislikes.stream().filter(ld -> !ld.isLike()).count() : 0);
    }
}
