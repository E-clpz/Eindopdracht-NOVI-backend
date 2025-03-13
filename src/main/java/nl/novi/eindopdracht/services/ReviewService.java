package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.dtos.ReviewDto;
import nl.novi.eindopdracht.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.models.Request;
import nl.novi.eindopdracht.models.Review;
import nl.novi.eindopdracht.models.User;
import nl.novi.eindopdracht.repositories.RequestRepository;
import nl.novi.eindopdracht.repositories.ReviewRepository;
import nl.novi.eindopdracht.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Scheduled(fixedRate = 86400000)
    public void scheduledRequestStatusCheck() {
        List<Request> allRequests = requestRepository.findAll();
        for (Request request : allRequests) {
            checkAndCloseRequest(request.getId());
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    public void checkAndCloseRequest(Long requestId) {
        Optional<Request> requestOptional = requestRepository.findById(requestId);
        if (requestOptional.isPresent()) {
            Request request = requestOptional.get();

            if (LocalDate.now().isAfter(request.getPreferredDate()) && !request.getStatus().equals("Gesloten")) {
                request.setStatus("Gesloten");
                requestRepository.save(request);
            }
        }
    }

    public List<ReviewDto> getReviewsByRequester(Long requesterId) {
        List<Review> reviews = reviewRepository.findByRequesterId(requesterId);
        return reviews.stream()
                .map(review -> {
                    Long requestId = review.getRequester().getId();
                    return new ReviewDto(review.getId(), review.getRequester().getId(), review.getHelper().getId(), review.getRating(), requestId);
                })
                .collect(Collectors.toList());
    }

    public ReviewDto addReview(Long requesterId, Long helperId, ReviewDto reviewDto) {
        logger.info("Requester {} voegt een review toe voor Helper {}", requesterId, helperId);

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Requester niet gevonden"));
        User helper = userRepository.findById(helperId)
                .orElseThrow(() -> new ResourceNotFoundException("Helper niet gevonden"));

        if (reviewDto.getRating() < 1 || reviewDto.getRating() > 5) {
            throw new IllegalArgumentException("De beoordeling moet tussen 1 en 5 sterren liggen.");
        }

        Request request = requestRepository.findByRequesterIdAndHelperId(requesterId, helperId)
                .orElseThrow(() -> new ResourceNotFoundException("Geen bijbehorende hulpvraag gevonden."));

        if (!"Gesloten".equalsIgnoreCase(request.getStatus())) {
            throw new IllegalStateException("Je kunt alleen een review achterlaten als de hulpvraag gesloten is.");
        }

        if (request.getHelper() == null) {
            throw new IllegalStateException("Je kunt geen review achterlaten als er geen helper aan de hulpvraag was gekoppeld.");
        }

        Review existingReview = reviewRepository.findByRequesterIdAndHelperId(requesterId, helperId);
        if (existingReview != null) {
            throw new IllegalStateException("Je hebt al een beoordeling voor deze helper gegeven.");
        }

        Review review = new Review(requester, helper, reviewDto.getRating());
        Review savedReview = reviewRepository.save(review);

        double averageRating = reviewRepository.findAverageRatingByHelperId(helperId);
        helper.setRating((int) averageRating);
        userRepository.save(helper);

        return new ReviewDto(savedReview.getId(), savedReview.getRequester().getId(), savedReview.getHelper().getId(), savedReview.getRating(), request.getId());
    }
}