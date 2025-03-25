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
    private UserDto helper;
    private LocalDate preferredDate;
    private String fileUrl;

    public RequestDto() {
    }

    public RequestDto(Long id, String title, String description, String category, String status, String city, Long requesterId, UserDto helper, LocalDate preferredDate, String fileUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.status = status;
        this.city = city;
        this.requesterId = requesterId;
        this.helper = helper;
        this.preferredDate = preferredDate;
        this.fileUrl = fileUrl;

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

    public UserDto getHelper() {
        return helper;
    }

    public void setHelper(UserDto helper) {
        this.helper = helper;
    }

    public LocalDate getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(LocalDate preferredDate) {
        this.preferredDate = preferredDate;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
