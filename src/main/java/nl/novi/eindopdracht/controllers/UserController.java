package nl.novi.eindopdracht.controllers;

import nl.novi.eindopdracht.dtos.UserDto;
import nl.novi.eindopdracht.models.User;
import nl.novi.eindopdracht.services.JwtService;
import nl.novi.eindopdracht.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(value = "id", required = false) Optional<Long> id,
            @RequestParam(value = "username", required = false) Optional<String> username,
            @RequestParam(value = "email", required = false) Optional<String> email) {

        List<User> users;

        if (id.isPresent()) {
            Optional<User> user = userService.getUserById(id.get());
            users = user.map(List::of).orElse(List.of());
        } else if (username.isPresent()) {
            Optional<User> user = userService.getUserByUsername(username.get());
            users = user.map(List::of).orElse(List.of());
        } else if (email.isPresent()) {
            Optional<User> user = userService.getUserByEmail(email.get());
            users = user.map(List::of).orElse(List.of());
        } else {
            users = userService.getAllUsers();
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("/my")
    public ResponseEntity<UserDto> getMyProfile(@AuthenticationPrincipal UserDetails user) {
        Long userIdFromToken = Long.parseLong(user.getUsername());
        UserDto userDto = userService.getUserDto(userIdFromToken, false, false, null);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
