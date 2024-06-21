package test.shopez.catalog.domain.review;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import test.shopez.catalog.domain.review.dto.likedislike.LikeDislikeCreateDTO;
import test.shopez.catalog.domain.review.dto.review.ReviewCreateDTO;
import test.shopez.catalog.domain.review.dto.review.ReviewResponseDTO;
import test.shopez.catalog.domain.review.entities.LikeDislike;
import test.shopez.catalog.domain.review.mapper.ReviewDTOMapper;
import test.shopez.catalog.error.exception.InternalServerException;
import test.shopez.catalog.error.exception.NotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewDTOMapper reviewDTOMapper;

    // TODO: Implement the pagination and data filtering
    public List<ReviewResponseDTO> findAll() {
        try {
            return reviewRepository.findAll().stream().map(reviewDTOMapper).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching reviews", e);
        }
    }

    public ReviewResponseDTO create(@NotNull ReviewCreateDTO payload) {
        try {
            Review review = Review
                    .builder()
                    .author(payload.getAuthor()) // pass the authenticated user instead
                    .productId(payload.getProduct()) // validate to check the productId
                    .note(payload.getNote())
                    .message(payload.getMessage())
                    .publishedDate(new Date())
                    .build();

            reviewRepository.save(review);

            return reviewDTOMapper.apply(review);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public Optional<ReviewResponseDTO> findById(String id) {
        Optional<Review> review = reviewRepository.findById(id);

        if (review.isEmpty()) {
            throw new NotFoundException("Review", id);
        }

        try {
            return review.map(reviewDTOMapper);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public ReviewResponseDTO update(String id, @NotNull ReviewCreateDTO payload) {
        Optional<Review> review = reviewRepository.findById(id);

        if (review.isEmpty()) {
            throw new NotFoundException("Review", id);
        }

        try {
            review.get().setMessage(payload.getMessage());
            review.get().setNote(payload.getNote());

            reviewRepository.save(review.get());

            return reviewDTOMapper.apply(review.get());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public void delete(String id) {
        Optional<Review> review = reviewRepository.findById(id);

        if (review.isEmpty()) {
            throw new NotFoundException("Review", id);
        }

        try {
            reviewRepository.delete(review.get());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public void likeDislike(String id, @NotNull LikeDislikeCreateDTO payload) {
        Optional<Review> review = reviewRepository.findById(id);

        if (review.isEmpty()) {
            throw new NotFoundException("Review", id);
        }

        try {
            Review reviewEntity = review.get();
            if (reviewEntity.getLikeDislike() == null) {
                reviewEntity.setLikeDislike(new ArrayList<>());
            }

            reviewEntity.getLikeDislike().removeIf(ld -> ld.getAuthorId().equals(payload.getAuthorId())); // remove the previous like/dislike

            LikeDislike ld = LikeDislike
                    .builder()
                    .authorId(payload.getAuthorId())
                    .like(payload.isLike())
                    .build(); //

            reviewEntity.getLikeDislike().add(ld);

            reviewRepository.save(reviewEntity);
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
