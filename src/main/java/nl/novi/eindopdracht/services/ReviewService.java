package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.dtos.ReviewDto;
import nl.novi.eindopdracht.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.models.Review;
import nl.novi.eindopdracht.models.User;
import nl.novi.eindopdracht.repositories.ReviewRepository;
import nl.novi.eindopdracht.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ReviewDto> getReviewsByRequester(Long requesterId) {
        List<Review> reviews = reviewRepository.findByRequesterId(requesterId);
        return reviews.stream()
                .map(review -> new ReviewDto(review.getId(), review.getRequester().getId(), review.getHelper().getId(), review.getRating(), review.getComment()))
                .collect(Collectors.toList());
    }

    public ReviewDto addReview(Long requesterId, Long helperId, ReviewDto reviewDto) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Requester niet gevonden"));
        User helper = userRepository.findById(helperId)
                .orElseThrow(() -> new ResourceNotFoundException("Helper niet gevonden"));

        Review review = new Review(requester, helper, reviewDto.getRating(), reviewDto.getComment());
        Review savedReview = reviewRepository.save(review);

        return new ReviewDto(savedReview.getId(), savedReview.getRequester().getId(), savedReview.getHelper().getId(), savedReview.getRating(), savedReview.getComment());
    }
}