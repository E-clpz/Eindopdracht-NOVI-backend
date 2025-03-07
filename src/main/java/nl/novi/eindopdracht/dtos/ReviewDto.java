package nl.novi.eindopdracht.dtos;

public class ReviewDto {

    private Long id;
    private Long requesterId;
    private Long helperId;
    private Integer rating;
    private String comment;

    public ReviewDto() {}

    public ReviewDto(Long id, Long requesterId, Long helperId, Integer rating, String comment) {
        this.id = id;
        this.requesterId = requesterId;
        this.helperId = helperId;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public Long getHelperId() {
        return helperId;
    }

    public void setHelperId(Long helperId) {
        this.helperId = helperId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}