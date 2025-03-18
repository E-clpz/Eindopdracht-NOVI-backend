package nl.novi.eindopdracht.controllers;

import nl.novi.eindopdracht.FileUploadResponse.FileUploadResponse;
import nl.novi.eindopdracht.dtos.RequestDto;
import nl.novi.eindopdracht.models.FileDocument;
import nl.novi.eindopdracht.models.Request;
import nl.novi.eindopdracht.repositories.RequestRepository;
import nl.novi.eindopdracht.services.FileService;
import nl.novi.eindopdracht.services.RequestService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
public class FileController {

    private final FileService fileService;
    private final RequestService requestService;
    private final RequestRepository requestRepository;

    public FileController(FileService fileService, RequestService requestService, RequestRepository requestRepository) {
        this.fileService = fileService;
        this.requestService = requestService;
        this.requestRepository = requestRepository;
    }

    @PostMapping("single/uploadDb")
    public FileUploadResponse singleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam Long requestId, @AuthenticationPrincipal UserDetails user) throws IOException {

        RequestDto requestDto = requestService.getRequestsById(requestId, user);
        if (requestDto == null) {
            throw new RuntimeException("Hulpvraag niet gevonden");
        }

        FileDocument fileDocument = fileService.uploadFile(file, requestId);

        if (fileDocument == null) {
            throw new RuntimeException("Bestand kon niet worden opgeslagen.");
        }

        Request request = requestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request niet gevonden"));

        if (request.getFile() != null) {
            request.setFile(null);
        }

        request.setFile(fileDocument);

        requestRepository.save(request);

        return new FileUploadResponse(fileDocument.getFileName(), fileDocument.getFileUrl(), fileDocument.getContentType());
    }

    @GetMapping("/downloadFromDB/{fileName}")
    ResponseEntity<byte[]> downLoadSingleFile(@PathVariable String fileName) {

        FileDocument document = fileService.getFileDocument(fileName);
        if (document == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + document.getFileName()).contentType(MediaType.valueOf(document.getContentType())).body(document.getFileData());
    }
}