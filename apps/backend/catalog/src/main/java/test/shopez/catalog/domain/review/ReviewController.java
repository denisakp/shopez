package test.shopez.catalog.domain.review;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.shopez.catalog.domain.review.dto.likedislike.LikeDislikeCreateDTO;
import test.shopez.catalog.domain.review.dto.review.ReviewCreateDTO;
import test.shopez.catalog.domain.review.dto.review.ReviewResponseDTO;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "${apiPrefix}/${apiVersion}/reviews", produces = "application/json")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> findAllHandler() {
        return new ResponseEntity<>(reviewService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createHandler(@Valid @RequestBody ReviewCreateDTO payload) {
        return new ResponseEntity<>(reviewService.create(payload), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ReviewResponseDTO>> findByIdHandler(@PathVariable String id) {
        return new ResponseEntity<>(reviewService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> updateHandler(@PathVariable String id, @Valid @RequestBody ReviewCreateDTO payload) {
        return new ResponseEntity<>(reviewService.update(id, payload), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHandler(@PathVariable String id) {
        reviewService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeDislikeHandler(@PathVariable String id, @Valid @RequestBody LikeDislikeCreateDTO payload) {
        reviewService.likeDislike(id, payload);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
