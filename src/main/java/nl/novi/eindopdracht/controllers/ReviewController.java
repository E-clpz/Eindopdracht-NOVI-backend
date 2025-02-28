package nl.novi.eindopdracht.controllers;

import nl.novi.eindopdracht.dtos.ReviewDto;
import nl.novi.eindopdracht.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/requester/{requesterId}")
    public List<ReviewDto> getReviewsByRequester(@PathVariable Long requesterId) {
        return reviewService.getReviewsByRequester(requesterId);
    }

    @PostMapping("/requester/{requesterId}/helper/{helperId}")
    public ReviewDto addReview(@PathVariable Long requesterId, @PathVariable Long helperId, @RequestBody ReviewDto reviewDto) {
        return reviewService.addReview(requesterId, helperId, reviewDto);
    }
}