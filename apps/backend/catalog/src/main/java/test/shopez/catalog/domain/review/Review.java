package test.shopez.catalog.domain.review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import test.shopez.catalog.domain.review.entities.LikeDislike;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Document(collection = "reviews")
@CompoundIndexes({
        @CompoundIndex(name = "product_author_idx", def = "{'productId': 1, 'author': 1}", unique = true)
})
public class Review {
    @Id
    private String id;

    @NotNull
    private String author;

    @NotBlank
    private String productId;

    private int note;

    private String message;

    private Date publishedDate;

    private List<LikeDislike> likeDislike;
}
