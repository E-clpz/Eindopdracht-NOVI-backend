package nl.novi.eindopdracht.dtos;

public class HelperDto {
    private Long id;
    private String username;
    private Integer rating;

    public HelperDto() {
    }

    public HelperDto(Long id, String username, Integer rating) {
        this.id = id;
        this.username = username;
        this.rating = rating;
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
