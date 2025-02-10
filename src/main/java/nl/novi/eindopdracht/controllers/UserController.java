package nl.novi.eindopdracht.controllers;

import nl.novi.eindopdracht.dtos.UserDto;
import nl.novi.eindopdracht.models.User;
import nl.novi.eindopdracht.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(value = "username", required = false) Optional<String> username,
            @RequestParam(value = "email", required = false) Optional<String> email) {

        List<User> users;

        if (username.isPresent()) {
            users = List.of(userService.getUserByUsername(username.get()).orElse(null));
        } else if (email.isPresent()) {
            users = List.of(userService.getUserByEmail(email.get()).orElse(null));
        } else {
            users = userService.getAllUsers();
        }
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable Long id,
            @RequestParam boolean isAdmin,
            @RequestParam boolean isRequesterAccepted) {
        UserDto userDto = userService.getUserDto(id, isAdmin, isRequesterAccepted);
        return ResponseEntity.ok().body(userDto);
    }

    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
