package nl.novi.eindopdracht.repositories;

import nl.novi.eindopdracht.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRequesterId(Long requesterId);
    Review findByRequesterIdAndHelperId(Long requesterId, Long helperId);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.helper.id = :helperId")
    double findAverageRatingByHelperId(@Param("helperId") Long helperId);
}