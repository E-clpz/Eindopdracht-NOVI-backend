package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.dtos.UserDto;
import nl.novi.eindopdracht.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.models.User;
import nl.novi.eindopdracht.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getUserDto(Long userId, boolean isAdmin, boolean isRequesterAccepted) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (isAdmin || isRequesterAccepted) {
            return new UserDto(user.getId(), user.getUsername(), user.getCity(), user.getEmail(), user.getPhoneNumber());
        } else {
            return new UserDto(user.getId(), user.getUsername(), user.getCity());
        }
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
