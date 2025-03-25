package nl.novi.eindopdracht.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "helper_id", nullable = false)
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private User helper;

    @Min(value = 1, message = "De beoordeling moet minimaal 1 ster zijn")
    @Max(value = 5, message = "De beoordeling mag maximaal 5 sterren zijn")
    private Integer rating;

    public Review() {
    }

    public Review(User requester, User helper, Integer rating) {
        this.requester = requester;
        this.helper = helper;
        this.rating = rating;
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

}