package nl.novi.eindopdracht.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "helper_id", nullable = false)
    private User helper;

    @Min(value = 1, message = "De beoordeling moet minimaal 1 zijn")
    @Max(value = 5, message = "De beoordeling moet maximaal 5 zijn")
    @Column(nullable = false)
    private Integer rating;

    @NotNull(message = "Review tekst mag niet leeg zijn")
    @Column(nullable = false)
    private String comment;

    public Review() {
    }

    public Review(User requester, User helper, Integer rating, String comment) {
        this.requester = requester;
        this.helper = helper;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getHelper() {
        return helper;
    }

    public void setHelper(User helper) {
        this.helper = helper;
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