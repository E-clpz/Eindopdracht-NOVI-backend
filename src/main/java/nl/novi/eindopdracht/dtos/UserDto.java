package nl.novi.eindopdracht.dtos;

public class UserDto {
    private Long id;
    private String username;
    private String city;
    private String phoneNumber;
    private String email;

    public UserDto(Long id, String username, String city) {
        this.id = id;
        this.username = username;
        this.city = city;
    }

    public UserDto(Long id, String username, String city, String email, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.city = city;
        this.email = email;
        this.phoneNumber = phoneNumber;
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
}
