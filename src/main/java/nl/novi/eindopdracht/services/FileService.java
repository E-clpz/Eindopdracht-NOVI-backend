package nl.novi.eindopdracht.services;

import jakarta.transaction.Transactional;
import nl.novi.eindopdracht.dtos.RequestDto;
import nl.novi.eindopdracht.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.models.FileDocument;
import nl.novi.eindopdracht.models.Request;
import nl.novi.eindopdracht.repositories.FileRepository;
import nl.novi.eindopdracht.repositories.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        fileDocument.setFileType(file.getContentType());
        fileDocument.setDocFile(file.getBytes());
        fileDocument.setRequest(request);

        if (request.getFiles() == null) {
            request.setFiles(new ArrayList<>());
        }
        request.getFiles().add(fileDocument);

        fileDocumentRepository.save(fileDocument);
        requestRepository.save(request);

        return fileDocument;
    }

    @Transactional
    public List<FileDocument> uploadMultipleFiles(MultipartFile[] files, @RequestParam Long requestId) throws IOException {
        List<FileDocument> fileDocuments = new ArrayList<>();
        for (MultipartFile file : files) {
            fileDocuments.add(uploadFile(file, requestId));
        }
        return fileDocuments;
    }

    @Transactional
    public List<FileDocument> getAllFromDB() {
        return fileDocumentRepository.findAll();
    }

    @Transactional
    public FileDocument getFileDocument(String fileName) {
        return fileDocumentRepository.findByFileName(fileName);
    }
}
