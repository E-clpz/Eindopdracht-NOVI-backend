package nl.novi.eindopdracht.dtos;

import java.time.LocalDate;

public class RequestDto {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String status;
    private String city;
    private Long requesterId;
    private HelperDto helper;
    private LocalDate preferredDate;
    private String requesterEmail;
    private String requesterPhoneNumber;
    private String helperEmail;
    private String helperPhoneNumber;

    public RequestDto() {
    }

    public RequestDto(Long id, String title, String description, String category, String status, String city, Long requesterId, HelperDto helper, LocalDate preferredDate, String requesterEmail, String requesterPhoneNumber, String helperEmail, String helperPhoneNumber) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.status = status;
        this.city = city;
        this.requesterId = requesterId;
        this.helper = helper;
        this.preferredDate = preferredDate;
        this.requesterEmail = requesterEmail;
        this.requesterPhoneNumber = requesterPhoneNumber;
        this.helperEmail = helperEmail;
        this.helperPhoneNumber = helperPhoneNumber;
    }

    // Getters and setters for all fields
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public HelperDto getHelper() {
        return helper;
    }

    public void setHelper(HelperDto helper) {
        this.helper = helper;
    }

    public LocalDate getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(LocalDate preferredDate) {
        this.preferredDate = preferredDate;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public String getRequesterPhoneNumber() {
        return requesterPhoneNumber;
    }

    public void setRequesterPhoneNumber(String requesterPhoneNumber) {
        this.requesterPhoneNumber = requesterPhoneNumber;
    }

    public String getHelperEmail() {
        return helperEmail;
    }

    public void setHelperEmail(String helperEmail) {
        this.helperEmail = helperEmail;
    }

    public String getHelperPhoneNumber() {
        return helperPhoneNumber;
    }

    public void setHelperPhoneNumber(String helperPhoneNumber) {
        this.helperPhoneNumber = helperPhoneNumber;
    }
}
