package nl.novi.eindopdracht.dtos;

public class HelperDto {
    private String username;
    private Integer rating;

    public HelperDto() {
    }

    public HelperDto(String name, Integer rating) {
        this.username = name;
        this.rating = rating;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}