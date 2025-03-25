package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.dtos.CreateUserDto;
import nl.novi.eindopdracht.dtos.UserDto;
import nl.novi.eindopdracht.exceptions.ConflictException;
import nl.novi.eindopdracht.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.models.Request;
import nl.novi.eindopdracht.models.Role;
import nl.novi.eindopdracht.models.User;
import nl.novi.eindopdracht.repositories.RequestRepository;
import nl.novi.eindopdracht.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Autowired
    public UserService(UserRepository userRepository, RequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
    }

    public UserDto getUserDto(UserDetails userDetails, boolean isAdmin) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User niet gevonden"));

        boolean isSelf = user.getUsername().equals(userDetails.getUsername());

        if (isAdmin || isSelf) {
            return new UserDto(user.getId(), user.getUsername(), user.getCity(), user.getEmail(), user.getPhoneNumber(), user.getRole(), user.getRating());
        } else {
            return new UserDto(user.getId(), user.getUsername(), user.getCity());
        }
    }

    public boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        boolean isValid = password.matches(passwordPattern);
        if (!isValid) {
            System.out.println("Fout wachtwoord: Wachtwoord moet minimaal 8 tekens bevatten, " + "met ten minste één kleine letter, één hoofdletter, één cijfer en " + "één speciaal teken (@#$%^&+=!).");
        }
        return isValid;
    }

    public CreateUserDto createUser(CreateUserDto createUserDto) {
        if (userRepository.existsByUsername(createUserDto.getUsername())) {
            throw new ConflictException("Gebruikersnaam is al in gebruik");
        }

        if (userRepository.existsByEmail(createUserDto.getEmail())) {
            throw new ConflictException("E-mailadres is al in gebruik.");
        }

        if (userRepository.existsByPhoneNumber(createUserDto.getPhoneNumber())) {
            throw new ConflictException("Telefoonnummer is al in gebruik.");
        }

        if (!isValidPassword(createUserDto.getPassword())) {
            throw new IllegalArgumentException("Wachtwoord moet minimaal 8 tekens lang zijn en minstens 1 kleine letter, 1 hoofdletter, 1 cijfer en 1 speciaal teken bevatten.");
        }

        Role role = createUserDto.getRole();

        User user = new User();
        user.setUsername(createUserDto.getUsername());
        user.setCity(createUserDto.getCity());
        user.setEmail(createUserDto.getEmail());
        user.setPhoneNumber(createUserDto.getPhoneNumber());
        user.setRole(role);
        user.setPassword(new BCryptPasswordEncoder().encode(createUserDto.getPassword()));

        User savedUser = userRepository.save(user);

        return new CreateUserDto(savedUser.getId(), savedUser.getUsername(), savedUser.getCity(), savedUser.getEmail(), savedUser.getPhoneNumber(), savedUser.getRole(), null);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Gebruiker niet gevonden"));

        if (userDto.getUsername() != null && !user.getUsername().equals(userDto.getUsername()) && userRepository.existsByUsername(userDto.getUsername())) {
            throw new ConflictException("Gebruikersnaam is al in gebruik.");
        }

        if (userDto.getEmail() != null && !user.getEmail().equals(userDto.getEmail()) && userRepository.existsByEmail(userDto.getEmail())) {
            throw new ConflictException("E-mailadres is al in gebruik.");
        }

        if (userDto.getPhoneNumber() != null && !user.getPhoneNumber().equals(userDto.getPhoneNumber()) && userRepository.existsByPhoneNumber(userDto.getPhoneNumber())) {
            throw new ConflictException("Telefoonnummer is al in gebruik.");
        }

        if (userDto.getUsername() != null) {
            user.setUsername(userDto.getUsername());
        }
        if (userDto.getCity() != null) {
            user.setCity(userDto.getCity());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getPhoneNumber() != null) {
            user.setPhoneNumber(userDto.getPhoneNumber());
        }

        User updatedUser = userRepository.save(user);

        return new UserDto(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getCity(), updatedUser.getEmail(), updatedUser.getPhoneNumber(), updatedUser.getRole(), user.getRating());
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Gebruiker niet gevonden"));

        List<Request> requests = new ArrayList<>();
        requests.addAll(requestRepository.findByRequester(user));
        requests.addAll(requestRepository.findByHelper(user));

        for (Request request : requests) {
            if (request.getHelper() != null && request.getHelper().equals(user)) {
                request.setHelper(null);
                request.setStatus("Open");
            }
        }

        requestRepository.saveAll(requests);
        requestRepository.deleteAll(requestRepository.findByRequester(user));
        userRepository.delete(user);
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
}
