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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private UserService userService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = new User("requester1", "requester1@example.com", "0612345678", "Amsterdam", "password", Role.REQUESTER, null);
        existingUser.setId(1L);
    }

    @Test
    void testCreateUser() {
        CreateUserDto newUserDto = new CreateUserDto(null, "requester2", "Groningen", "requester2@example.com", "0612345679", Role.REQUESTER, "Password123!");

        User newUser = new User("requester2", "requester2@example.com", "0612345679", "Groningen", "Password123!", Role.REQUESTER, null);
        newUser.setId(1L);

        when(userRepository.save(any(User.class))).thenReturn(newUser);

        CreateUserDto createdUserDto = userService.createUser(newUserDto);

        assertNotNull(createdUserDto);
        assertEquals("requester2", createdUserDto.getUsername());
        assertEquals("requester2@example.com", createdUserDto.getEmail());
        assertEquals("Groningen", createdUserDto.getCity());
        assertEquals("0612345679", createdUserDto.getPhoneNumber());
        assertEquals(Role.REQUESTER, createdUserDto.getRole());
    }

    @Test
    void testCreateUserWithExistingUsername() {
        CreateUserDto newUserDto = new CreateUserDto(null, "requester1", "Groningen", "requester2@example.com", "0612345679", Role.REQUESTER, "Password123!");

        when(userRepository.existsByUsername("requester1")).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.createUser(newUserDto));
    }

    @Test
    void testCreateUserWithExistingEmail() {
        CreateUserDto newUserDto = new CreateUserDto(null, "requester2", "Groningen", "requester1@example.com", "0612345679", Role.REQUESTER, "Password123!");

        when(userRepository.existsByEmail("requester1@example.com")).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.createUser(newUserDto));
    }

    @Test
    void testCreateUserWithExistingPhoneNumber() {
        CreateUserDto newUserDto = new CreateUserDto(null, "requester2", "Groningen", "requester2@example.com", "0612345678", Role.REQUESTER, "Password123!");

        when(userRepository.existsByPhoneNumber("0612345678")).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.createUser(newUserDto));
    }

    @Test
    void testCreateUserWithoutValidPassword() {
        CreateUserDto newUserDto = new CreateUserDto(null, "requester3", "Amsterdam", "requester3@example.com", "0612345679", Role.REQUESTER, "password");

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(newUserDto));
    }

    @Test
    void testGetMyProfile() {

        User mockUser = new User();
        mockUser.setUsername("Rob Arentz");
        mockUser.setCity("TestCity");
        mockUser.setEmail("test@test.com");
        mockUser.setPhoneNumber("1234567890");
        mockUser.setRole(Role.REQUESTER);

        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("Rob Arentz");

        when(userRepository.findByUsername("Rob Arentz")).thenReturn(Optional.of(mockUser));

        UserDto userDto = userService.getUserDto(mockUserDetails, false);

        assertNotNull(userDto);
        assertEquals("Rob Arentz", userDto.getUsername());
    }

    @Test
    void testGetUserDtoWhenAdminButNotSelf() {
        User otherUser = new User("otherUser", "otheruser@example.com", "0612345679", "OtherCity", "other123!", Role.REQUESTER, null);
        otherUser.setId(2L);

        UserDetails mockAdminDetails = mock(UserDetails.class);
        when(mockAdminDetails.getUsername()).thenReturn("adminUser");

        when(userRepository.findByUsername("adminUser")).thenReturn(Optional.of(otherUser));

        UserDto userDto = userService.getUserDto(mockAdminDetails, true);

        assertNotNull(userDto);
        assertEquals("otherUser", userDto.getUsername());
        assertEquals("OtherCity", userDto.getCity());
        assertEquals("otheruser@example.com", userDto.getEmail());
        assertEquals("0612345679", userDto.getPhoneNumber());
    }

    @Test
    void testGetUserDtoWhenNotSelfAndNotAdmin() {
        User otherUser = new User("otherUser", "otheruser@example.com", "0612345679", "OtherCity", "other123!", Role.REQUESTER, null);
        otherUser.setId(2L);

        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("mockUser");

        when(userRepository.findByUsername("mockUser")).thenReturn(Optional.of(otherUser));

        UserDto userDto = userService.getUserDto(mockUserDetails, false);

        assertNotNull(userDto);
        assertEquals("otherUser", userDto.getUsername());
        assertEquals("OtherCity", userDto.getCity());
        assertNull(userDto.getEmail());
        assertNull(userDto.getPhoneNumber());
    }

    @Test
    void testIsValidPassword() {
        assertFalse(userService.isValidPassword("password"));
        assertTrue(userService.isValidPassword("Password123!"));
    }

    @Test
    void testUpdateUser() {
        UserDto updateUserDto = new UserDto();
        updateUserDto.setUsername("updatedRequester");
        updateUserDto.setEmail("updated@example.com");
        updateUserDto.setPhoneNumber("0612345679");
        updateUserDto.setCity("UpdatedCity");
        updateUserDto.setRole(Role.REQUESTER);

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        UserDto result = userService.updateUser(1L, updateUserDto);

        assertNotNull(result);
        assertEquals("updatedRequester", result.getUsername());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("0612345679", result.getPhoneNumber());
        assertEquals("UpdatedCity", result.getCity());
        assertEquals(Role.REQUESTER, result.getRole());

        verify(userRepository).findById(1L);
        verify(userRepository).save(existingUser);
    }

    @Test
    void testUpdateUserWithExistingUsername() {
        UserDto updateUserDto = new UserDto();
        updateUserDto.setUsername("requester1");

        existingUser.setUsername("existingUsername");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("requester1")).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.updateUser(1L, updateUserDto));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUserWithExistingPhoneNumber() {
        UserDto updateUserDto = new UserDto();
        updateUserDto.setPhoneNumber("0612345678");

        existingUser.setPhoneNumber("0612345677");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByPhoneNumber("0612345678")).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.updateUser(1L, updateUserDto));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUserWithExistingEmail() {
        UserDto updateUserDto = new UserDto();
        updateUserDto.setEmail("existing@example.com");

        existingUser.setEmail("old@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.updateUser(1L, updateUserDto));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUserWithNoRequests() {
        User userToDelete = new User("requester1", "requester1@example.com", "0612345678", "Amsterdam", "password", Role.REQUESTER, null);
        userToDelete.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userToDelete));
        when(requestRepository.findByRequester(userToDelete)).thenReturn(new ArrayList<>());
        when(requestRepository.findByHelper(userToDelete)).thenReturn(new ArrayList<>());

        userService.deleteUser(1L);

        verify(userRepository).delete(userToDelete);
    }

    @Test
    void testDeleteUserWithRequests() {
        User userToDelete = new User("requester1", "requester1@example.com", "0612345678", "Amsterdam", "password", Role.REQUESTER, null);
        userToDelete.setId(1L);

        Request request = new Request();
        request.setRequester(userToDelete);
        request.setHelper(userToDelete);
        request.setStatus("Closed");

        List<Request> requests = new ArrayList<>();
        requests.add(request);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userToDelete));
        when(requestRepository.findByRequester(userToDelete)).thenReturn(requests);
        when(requestRepository.findByHelper(userToDelete)).thenReturn(requests);

        userService.deleteUser(1L);
        verify(requestRepository, times(1)).saveAll(anyList());

        verify(userRepository).delete(userToDelete);
    }

    @Test
    void testDeleteUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void testGetUserMethods() {
        User user = new User("requester1", "requester1@example.com", "0612345678", "Amsterdam", "password", Role.REQUESTER, null);
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("requester1")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("requester1@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        Optional<User> userById = userService.getUserById(1L);
        assertTrue(userById.isPresent());
        assertEquals(user.getId(), userById.get().getId());

        Optional<User> userByUsername = userService.getUserByUsername("requester1");
        assertTrue(userByUsername.isPresent());
        assertEquals(user.getUsername(), userByUsername.get().getUsername());

        Optional<User> userByEmail = userService.getUserByEmail("requester1@example.com");
        assertTrue(userByEmail.isPresent());
        assertEquals(user.getEmail(), userByEmail.get().getEmail());

        Optional<User> userNotFound = userService.getUserByUsername("nonexistent");
        assertFalse(userNotFound.isPresent());

        verify(userRepository).findById(1L);
        verify(userRepository).findByUsername("requester1");
        verify(userRepository).findByEmail("requester1@example.com");
        verify(userRepository).findByUsername("nonexistent");
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User("requester1", "requester1@example.com", "0612345678", "Amsterdam", "password", Role.REQUESTER, null);
        user1.setId(1L);

        User user2 = new User("requester2", "requester2@example.com", "0612345679", "Groningen", "password", Role.REQUESTER, null);
        user2.setId(2L);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("requester1", result.get(0).getUsername());
        assertEquals("requester2", result.get(1).getUsername());

        verify(userRepository).findAll();
    }
}