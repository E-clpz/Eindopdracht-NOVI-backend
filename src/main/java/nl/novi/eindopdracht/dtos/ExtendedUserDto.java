package nl.novi.eindopdracht.dtos;

import nl.novi.eindopdracht.models.Role;

public class ExtendedUserDto extends UserDto {
    private String email;
    private String phoneNumber;

    public ExtendedUserDto(Long id, String username, String city, String email, String phoneNumber, Role role) {
        super(id, username, city, role);
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}