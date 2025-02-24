package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.dtos.UserDto;
import nl.novi.eindopdracht.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.exceptions.UsernameAlreadyExistsException;
import nl.novi.eindopdracht.models.Role;
import nl.novi.eindopdracht.models.User;
import nl.novi.eindopdracht.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
                .orElseThrow(() -> new ResourceNotFoundException("User niet gevonden"));

        if (isAdmin || isRequesterAccepted) {
            return new UserDto(user.getId(), user.getUsername(), user.getCity(), user.getEmail(), user.getPhoneNumber(), user.getRole(), user.getPassword());
        } else {
            return new UserDto(user.getId(), user.getUsername(), user.getCity(), user.getRole());
        }
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        boolean isValid = password.matches(passwordPattern);
        if (!isValid) {
            System.out.println("Fout wachtwoord: " + password);
        }
        return isValid;
    }

    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Gebruikersnaam is al in gebruik");
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("E-mailadres is al in gebruik.");
        }
        if (!isValidPassword(userDto.getPassword())) {
            throw new IllegalArgumentException("Wachtwoord moet minimaal 8 tekens lang zijn en minstens 1 hoofdletter, 1 cijfer en 1 speciaal teken bevatten.");
        }

        Role role = userDto.getRole();
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setCity(userDto.getCity());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setRole(role);
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));

        User savedUser = userRepository.save(user);

        return new UserDto(savedUser.getId(), savedUser.getUsername(), savedUser.getCity(), savedUser.getEmail(), savedUser.getPhoneNumber(), savedUser.getRole(), savedUser.getPassword());
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
