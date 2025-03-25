package nl.novi.eindopdracht.FileUploadResponse;

public class FileUploadResponse {

    String fileName;
    String url;
    String contentType;

    public FileUploadResponse(String fileName, String url, String contentType) {
        this.fileName = fileName;
        this.url = url;
        this.contentType = contentType;

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
