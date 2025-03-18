package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.dtos.CreateUserDto;
import nl.novi.eindopdracht.exceptions.ConflictException;
import nl.novi.eindopdracht.models.User;
import nl.novi.eindopdracht.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public AuthService(JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    public String registerUser(CreateUserDto createUserDto) {
        if (userRepository.findByUsername(createUserDto.getUsername()).isPresent()) {
            throw new ConflictException("Gebruikersnaam bestaat al!");
        }

        User newUser = new User();
        newUser.setUsername(createUserDto.getUsername());
        newUser.setEmail(createUserDto.getEmail());
        newUser.setPhoneNumber(createUserDto.getPhoneNumber());
        newUser.setCity(createUserDto.getCity());
        newUser.setRole(createUserDto.getRole());

        newUser.setPassword(passwordEncoder.encode(createUserDto.getPassword()));

        userRepository.save(newUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername(newUser.getUsername());

        Long userId = newUser.getId();

        return jwtService.generateToken(userDetails, userId, 1000 * 60 * 60 * 24 * 10L);
    }
}

