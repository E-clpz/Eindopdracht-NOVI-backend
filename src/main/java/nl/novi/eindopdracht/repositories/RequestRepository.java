package nl.novi.eindopdracht.repositories;

import nl.novi.eindopdracht.models.Category;
import nl.novi.eindopdracht.models.Request;
import nl.novi.eindopdracht.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByCategory(Category category);
    List<Request> findByCity(String city);
    List<Request> findAllByOrderByCreatedAtAsc();
    List<Request> findAllByOrderByCreatedAtDesc();
    List<Request> findByRequester(User requester);
}
