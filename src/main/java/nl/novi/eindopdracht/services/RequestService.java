package nl.novi.eindopdracht.services;

import jakarta.transaction.Transactional;
import nl.novi.eindopdracht.dtos.RequestDto;
import nl.novi.eindopdracht.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.exceptions.UnauthorizedException;
import nl.novi.eindopdracht.mappers.RequestMapper;
import nl.novi.eindopdracht.models.Category;
import nl.novi.eindopdracht.models.Request;
import nl.novi.eindopdracht.models.Role;
import nl.novi.eindopdracht.models.User;
import nl.novi.eindopdracht.repositories.CategoryRepository;
import nl.novi.eindopdracht.repositories.RequestRepository;
import nl.novi.eindopdracht.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository, CategoryRepository categoryRepository, RequestMapper requestMapper, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.categoryRepository = categoryRepository;
        this.requestMapper = requestMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    public RequestDto acceptRequest(Long requestId, User helper) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Hulpvraag niet gevonden"));

        request.setHelper(helper);

        request = requestRepository.save(request);

        return requestMapper.toDto(request);
    }

    public List<RequestDto> getAllRequestsForHelpers(String categoryName, String city, String sortByDate) {
        List<Request> requests;

        if (categoryName != null) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new ResourceNotFoundException("Categorie niet gevonden: " + categoryName));
            requests = requestRepository.findByCategory(category);
        } else {
            requests = requestRepository.findAll();
        }

        if (city != null) {
            requests = requests.stream()
                    .filter(request -> request.getCity().equalsIgnoreCase(city))
                    .collect(Collectors.toList());
        }

        if ("asc".equalsIgnoreCase(sortByDate)) {
            requests.sort((r1, r2) -> r1.getPreferredDate().compareTo(r2.getPreferredDate()));
        } else if ("desc".equalsIgnoreCase(sortByDate)) {
            requests.sort((r1, r2) -> r2.getPreferredDate().compareTo(r1.getPreferredDate()));
        }

        return requests.stream().map(requestMapper::toDto).collect(Collectors.toList());
    }

    public List<RequestDto> getRequestsForRequester(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(()
                -> new ResourceNotFoundException("User niet gevonden"));
        List<Request> requests = requestRepository.findByRequester(user);
        return requests.stream().map(requestMapper::toDto).collect(Collectors.toList());
    }

    public RequestDto updateRequest(Long id, RequestDto requestDto, UserDetails user) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request met id: " + id + " niet gevonden"));

        if (!(request.getRequester().getUsername().equals(user.getUsername()) ||
                user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))) {
            throw new UnauthorizedException("Je mag alleen je eigen hulpvragen bijwerken, of je moet admin zijn.");
        }

        request.setTitle(requestDto.getTitle());
        request.setDescription(requestDto.getDescription());
        request.setPreferredDate(requestDto.getPreferredDate());
        request.setCity(requestDto.getCity());
        request.setStatus(requestDto.getStatus());

        Category category = categoryRepository.findByName(requestDto.getCategory())
                .orElseThrow(() -> new ResourceNotFoundException("Categorie niet gevonden: " + requestDto.getCategory()));
        request.setCategory(category);

        requestRepository.save(request);
        return requestMapper.toDto(request);
    }

    public void deleteRequest(Long id, UserDetails user) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request met id: " + id + " niet gevonden"));
        if (!request.getRequester().getUsername().equals(user.getUsername()) && user.getAuthorities().stream()
                .noneMatch(auth -> auth.getAuthority().contains(Role.ADMIN.toString()))) {
            throw new UnauthorizedException("Je mag alleen je eigen hulpvragen verwijderen, of je moet admin zijn.");
        }
        requestRepository.delete(request);
    }


    public RequestDto createRequest(RequestDto requestDto, UserDetails userDetails) {
        Category category = categoryRepository.findByName(requestDto.getCategory())
                .orElseThrow(() -> new ResourceNotFoundException("Categorie niet gevonden: " + requestDto.getCategory()));
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(()
                -> new ResourceNotFoundException("User niet gevonden"));
        Request request = requestMapper.toEntity(requestDto, category, user);
        request.setStatus("OPEN");
        request.setRequester(user);

        request = requestRepository.save(request);

        return requestMapper.toDto(request);
    }

    public RequestDto getRequestsById(long id, UserDetails user) {
        Request request = requestRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Request id: " + id + " niet gevonden"));
        if (!request.getRequester().getUsername().equals(user.getUsername())) {
            throw new UnauthorizedException("Je bent niet gemachtigd deze request te bekijken");
        }
        return requestMapper.toDto(request);
    }
}
