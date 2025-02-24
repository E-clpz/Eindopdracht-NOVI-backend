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

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable Long id,
            @RequestParam(value = "isAdmin", required = false, defaultValue = "false") boolean isAdmin,
            @RequestParam(value = "isRequesterAccepted", required = false, defaultValue = "false") boolean isRequesterAccepted) {

        UserDto userDto = userService.getUserDto(id, isAdmin, isRequesterAccepted);
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
