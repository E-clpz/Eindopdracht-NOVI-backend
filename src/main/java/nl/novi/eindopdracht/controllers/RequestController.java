package nl.novi.eindopdracht.controllers;

import jakarta.validation.Valid;
import nl.novi.eindopdracht.dtos.RequestDto;
import nl.novi.eindopdracht.exceptions.UnauthorizedException;
import nl.novi.eindopdracht.models.Role;
import nl.novi.eindopdracht.models.User;
import nl.novi.eindopdracht.repositories.UserRepository;
import nl.novi.eindopdracht.services.RequestService;
import nl.novi.eindopdracht.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {
    private final RequestService requestService;
    private final UserRepository userRepository;

    public RequestController(RequestService requestService, UserRepository userRepository) {
        this.requestService = requestService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<RequestDto> getAllRequestsForHelpers(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String sortByDate)
             {

        return requestService.getAllRequestsForHelpers(category, city, sortByDate);
    }

    @GetMapping("/my")
    public List<RequestDto> getRequestsForRequester(@AuthenticationPrincipal UserDetails user) {
        return requestService.getRequestsForRequester(user);
    }

    @PostMapping
    public ResponseEntity<RequestDto> createRequest(
            @Valid @RequestBody RequestDto requestDto,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {

        if (userDetails == null) {
            throw new UnauthorizedException("Gebruiker niet gevonden");
        }

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Gebruiker niet gevonden in database"));

        requestDto.setRequesterId(user.getId());

        RequestDto createdRequest = requestService.createRequest(requestDto, userDetails);
        return ResponseEntity.status(201).body(createdRequest);
    }


    @PutMapping("/{id}")
    public RequestDto updateRequest(@PathVariable Long id, @Valid @RequestBody RequestDto requestDTO, @AuthenticationPrincipal UserDetails user) {
        return requestService.updateRequest(id, requestDTO, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        requestService.deleteRequest(id, user);
        return ResponseEntity.noContent().build();
    }
}
