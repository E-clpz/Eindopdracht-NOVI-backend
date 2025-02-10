package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.dtos.RequestDto;
import nl.novi.eindopdracht.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.exceptions.UnauthorizedException;
import nl.novi.eindopdracht.models.Request;
import nl.novi.eindopdracht.models.User;
import nl.novi.eindopdracht.repositories.RequestRepository;
import nl.novi.eindopdracht.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    public List<RequestDto> getAllRequestsForHelpers(String category, String city, String sortByDate, User user) {
        if (!user.getRole().equals("HELPER")) {
            throw new UnauthorizedException("Alleen helpers mogen alle hulpvragen bekijken.");
        }

        List<Request> requests = requestRepository.findAll();

        if (category != null) {
            requests = requests.stream()
                    .filter(request -> request.getCategory().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
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
        request.setCategory(requestDto.getCategory());
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
                request.getCategory(),
                request.getStatus(),
                request.getCity(),
                request.getRequester().getId(),
                request.getHelper() != null ? request.getHelper().getId() : null
        );
    }
}
