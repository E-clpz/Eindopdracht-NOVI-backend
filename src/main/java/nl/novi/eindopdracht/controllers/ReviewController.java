package nl.novi.eindopdracht.controllers;

import jakarta.validation.Valid;
import nl.novi.eindopdracht.dtos.ReviewDto;
import nl.novi.eindopdracht.services.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Validated
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/requester/{requesterId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByRequester(@PathVariable Long requesterId) {
        List<ReviewDto> reviews = reviewService.getReviewsByRequester(requesterId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/requester/{requesterId}/helper/{helperId}")
    public ResponseEntity<ReviewDto> addReview(@PathVariable Long requesterId, @PathVariable Long helperId, @Valid @RequestBody ReviewDto reviewDto) {

        ReviewDto savedReview = reviewService.addReview(requesterId, helperId, reviewDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }
}