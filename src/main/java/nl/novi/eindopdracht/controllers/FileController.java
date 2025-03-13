package nl.novi.eindopdracht.controllers;

import jakarta.servlet.http.HttpServletRequest;
import nl.novi.eindopdracht.dtos.RequestDto;
import nl.novi.eindopdracht.models.FileDocument;
import nl.novi.eindopdracht.FileUploadResponse.FileUploadResponse;
import nl.novi.eindopdracht.services.RequestService;
import nl.novi.eindopdracht.services.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.io.IOException;
import java.util.*;

@CrossOrigin
@RestController
public class FileController {

    private final FileService fileService;
    private final RequestService requestService;

    public FileController(FileService fileService, RequestService requestService) {
        this.fileService = fileService;
        this.requestService = requestService;
    }

    @PostMapping("single/uploadDb")
    public FileUploadResponse singleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam Long requestId, @AuthenticationPrincipal UserDetails user) throws IOException {

        RequestDto requestDto = requestService.getRequestsById(requestId, user);
        if (requestDto == null) {
            throw new RuntimeException("Hulpvraag niet gevonden");
        }

        FileDocument fileDocument = fileService.uploadFile(file, requestDto);
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFromDB/")
                .path(Objects.requireNonNull(file.getOriginalFilename()))
                .toUriString();

        String contentType = file.getContentType();

        return new FileUploadResponse(fileDocument.getFileName(), url, contentType);
    }

    @GetMapping("/downloadFromDB/{fileName}")
    ResponseEntity<byte[]> downLoadSingleFile(@PathVariable String fileName) {

        FileDocument document = fileService.getFileDocument(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + document.getFileName())
                .contentType(MediaType.valueOf(document.getFileType()))
                .body(document.getDocFile());
    }

    @PostMapping("/multiple/upload/db")
    public List<FileUploadResponse> multipleUpload(@RequestParam("files") MultipartFile[] files, @RequestParam Long requestId, @AuthenticationPrincipal UserDetails user) throws IOException {

        RequestDto requestDto = requestService.getRequestsById(requestId, user);
        if (requestDto == null) {
            throw new RuntimeException("Hulpvraag niet gevonden");
        }

        List<FileDocument> fileDocuments = fileService.uploadMultipleFiles(files, requestDto);

        List<FileUploadResponse> fileUploadResponses = new ArrayList<>();
        for (FileDocument fileDocument : fileDocuments) {
            String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFromDB/")
                    .path(fileDocument.getFileName())
                    .toUriString();
            fileUploadResponses.add(new FileUploadResponse(fileDocument.getFileName(), url, fileDocument.getFileType()));
        }

        return fileUploadResponses;
    }

    @GetMapping("/getAll/db")
    public Collection<FileDocument> getAllFromDB() {
        return fileService.getAllFromDB();
    }
}