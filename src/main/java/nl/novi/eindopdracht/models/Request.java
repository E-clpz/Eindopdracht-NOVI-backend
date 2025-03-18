package nl.novi.eindopdracht.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    @NotNull(message = "Titel mag niet leeg zijn")
    @Size(min = 3, max = 30, message = "Titel moet tussen de 3 and 30 tekens bevatten")
    private String title;

    @Column(nullable = false, length = 250)
    @NotNull(message = "Omschrijving mag niet leeg zijn")
    @Size(min = 10, max = 250, message = "Gebruik tussen de 10 en 250 tekens")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String city;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = true)
    private User requester;

    @ManyToOne(optional = true)
    @JoinColumn(name = "helper_id")
    private User helper;

    @Column(nullable = false)
    private LocalDate preferredDate;

    @Column(nullable = true)
    private String fileName;

    @OneToOne(mappedBy = "request", cascade = CascadeType.ALL)
    private FileDocument file;

    public Request() {
    }

    public Request(String title, String description, Category category, String status, String city, User requester, LocalDate preferredDate, String fileName) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.status = status;
        this.city = city;
        this.requester = requester;
        this.preferredDate = preferredDate;
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public LocalDate getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(LocalDate preferredDate) {
        this.preferredDate = preferredDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileDocument getFile() {
        return file;
    }

    public void setFile(FileDocument file) {
        this.file = file;
    }
}
