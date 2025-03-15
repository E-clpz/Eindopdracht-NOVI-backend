package nl.novi.eindopdracht.models;

import jakarta.persistence.*;

@Entity
public class FileDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @Lob
    private byte[] fileData;

    private String contentType;

    private String fileUrl;

    @OneToOne
    @JoinColumn(name = "request_id")
    private Request request;

    public FileDocument() {
    }

    public FileDocument(String fileName, byte[] fileData, String contentType, Request request, String fileUrl) {
        this.fileName = fileName;
        this.fileData = fileData;
        this.contentType = contentType;
        this.request = request;
        this.fileUrl = fileUrl;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
