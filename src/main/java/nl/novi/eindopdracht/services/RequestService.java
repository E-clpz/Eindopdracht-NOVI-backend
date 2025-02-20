package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.dtos.RequestDto;
import nl.novi.eindopdracht.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.exceptions.UnauthorizedException;
import nl.novi.eindopdracht.models.Category;
import nl.novi.eindopdracht.models.Request;
import nl.novi.eindopdracht.models.User;
import nl.novi.eindopdracht.repositories.CategoryRepository;
import nl.novi.eindopdracht.repositories.RequestRepository;
import nl.novi.eindopdracht.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository, CategoryRepository categoryRepository) {
        this.requestRepository = requestRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<RequestDto> getAllRequestsForHelpers(String categoryName, String city, String sortByDate) {

        List<Request> requests = requestRepository.findAll();

        if (categoryName != null) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new ResourceNotFoundException("Categorie niet gevonden: " + categoryName));

            requests = requestRepository.findByCategory(category);
        }

        if (city != null) {
            requests = requests.stream()
                    .filter(request -> request.getCity().equalsIgnoreCase(city))
                    .collect(Collectors.toList());
        }

        if ("asc".equalsIgnoreCase(sortByDate)) {
            requests.sort((r1, r2) -> r1.getCreatedAt().compareTo(r2.getCreatedAt()));
        } else if ("desc".equalsIgnoreCase(sortByDate)) {
            requests.sort((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()));
        }

        return requests.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<RequestDto> getRequestsForRequester(User user) {
        List<Request> requests = requestRepository.findByRequester(user);
        return requests.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public RequestDto updateRequest(Long id, RequestDto requestDto, User user) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request met id: " + id + " niet gevonden"));

        if (!request.getRequester().equals(user)) {
            throw new UnauthorizedException("Je mag alleen je eigen hulpvragen bijwerken.");
        }

        request.setTitle(requestDto.getTitle());
        request.setDescription(requestDto.getDescription());
        Category category = categoryRepository.findByName(requestDto.getCategory())
                .orElseThrow(() -> new ResourceNotFoundException("Categorie niet gevonden: " + requestDto.getCategory()));
        request.setCategory(category);
        request.setStatus(requestDto.getStatus());
        request.setCity(requestDto.getCity());

        requestRepository.save(request);
        return convertToDto(request);
    }

    public void deleteRequest(Long id, User user) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request met id: " + id + " niet gevonden"));

        if (!request.getRequester().equals(user)) {
            throw new UnauthorizedException("Je mag alleen je eigen hulpvragen verwijderen.");
        }

        requestRepository.delete(request);
    }

    private RequestDto convertToDto(Request request) {
        return new RequestDto(
                request.getId(),
                request.getTitle(),
                request.getDescription(),
                request.getCategory().getName(),
                request.getStatus(),
                request.getCity(),
                request.getRequester().getId(),
                request.getHelper() != null ? request.getHelper().getId() : null
        );
    }
}
