package nl.novi.eindopdracht.services;

import jakarta.transaction.Transactional;
import nl.novi.eindopdracht.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.models.FileDocument;
import nl.novi.eindopdracht.models.Request;
import nl.novi.eindopdracht.repositories.FileRepository;
import nl.novi.eindopdracht.repositories.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@Service
public class FileService {

    private final FileRepository fileDocumentRepository;
    private final RequestRepository requestRepository;

    public FileService(FileRepository fileDocumentRepository, RequestRepository requestRepository) {
        this.fileDocumentRepository = fileDocumentRepository;
        this.requestRepository = requestRepository;
    }

    @Transactional
    public FileDocument uploadFile(MultipartFile file, @RequestParam Long requestId) throws IOException {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request niet gevonden"));

        FileDocument fileDocument = new FileDocument();
        fileDocument.setFileName(file.getOriginalFilename());
        fileDocument.setContentType(file.getContentType());
        fileDocument.setFileData(file.getBytes());
        fileDocument.setRequest(request);

        if (request.getFile() != null) {
            fileDocumentRepository.delete(request.getFile());
        }

        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFromDB/")
                .path(fileDocument.getFileName())
                .toUriString();

        fileDocument.setFileUrl(fileUrl);

        fileDocumentRepository.save(fileDocument);
        request.setFile(fileDocument);

        requestRepository.save(request);

        return fileDocument;
    }

    @Transactional
    public FileDocument getFileDocument(String fileName) {
        return fileDocumentRepository.findByFileName(fileName);
    }
}
