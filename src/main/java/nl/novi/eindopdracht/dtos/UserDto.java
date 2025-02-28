package nl.novi.eindopdracht.dtos;

import nl.novi.eindopdracht.models.Role;

public class UserDto {
    private Long id;
    private String username;
    private String city;
    private String email;
    private String phoneNumber;
    private Role role;
    private String password;
    private Long requesterId;

    public UserDto() {
    }

    public UserDto(Long id, String username, String city, Role role) {
        this.id = id;
        this.username = username;
        this.city = city;
        this.role = role;
    }

    public UserDto(Long id, String username, String city, String email, String phoneNumber, Role role, String password) {
        this.id = id;
        this.username = username;
        this.city = city;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.password = password;
    }

    public UserDto(Long id, String username, String city, String email, String phoneNumber, Role role, String password, Long requesterId) {
        this.id = id;
        this.username = username;
        this.city = city;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.password = password;
        this.requesterId = requesterId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }
}

