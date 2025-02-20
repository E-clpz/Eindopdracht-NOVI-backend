package nl.novi.eindopdracht.controllers;

import jakarta.validation.Valid;
import nl.novi.eindopdracht.dtos.RequestDto;
import nl.novi.eindopdracht.exceptions.UnauthorizedException;
import nl.novi.eindopdracht.models.Role;
import nl.novi.eindopdracht.models.User;
import nl.novi.eindopdracht.services.RequestService;
import nl.novi.eindopdracht.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {
    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public List<RequestDto> getAllRequestsForHelpers(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String sortByDate,
            @AuthenticationPrincipal User user) {

        return requestService.getAllRequestsForHelpers(category, city, sortByDate);
    }

    @GetMapping("/my")
    public List<RequestDto> getRequestsForRequester(@AuthenticationPrincipal User user) {
        return requestService.getRequestsForRequester(user);
    }

    @PutMapping("/{id}")
    public RequestDto updateRequest(@PathVariable Long id, @Valid @RequestBody RequestDto requestDTO, @AuthenticationPrincipal User user) {
        return requestService.updateRequest(id, requestDTO, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id, @AuthenticationPrincipal User user) {
        requestService.deleteRequest(id, user);
        return ResponseEntity.noContent().build();
    }
}
