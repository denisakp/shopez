package test.shopez.catalog.domain.review.mapper;

import org.springframework.stereotype.Service;
import test.shopez.catalog.domain.review.Review;
import test.shopez.catalog.domain.review.dto.review.ReviewResponseDTO;

import java.util.function.Function;

@Service
public class ReviewDTOMapper implements Function<Review, ReviewResponseDTO> {
    @Override
    public ReviewResponseDTO apply(Review review) {
        return new ReviewResponseDTO(
                review.getId(),
                review.getAuthor(),
                review.getNote(),
                review.getMessage(),
                review.getLikeDislike(),
                review.getPublishedDate()
        );
    }
}
