package nl.novi.eindopdracht.repositories;

import nl.novi.eindopdracht.models.Category;
import nl.novi.eindopdracht.models.Request;
import nl.novi.eindopdracht.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByCategory(Category category);
    List<Request> findByRequester(User requester);
    List<Request> findByHelper(User helper);
    Optional<Request> findById(Long id);
    Optional<Request> findByRequesterIdAndHelperId(Long requesterId, Long helperId);
}
