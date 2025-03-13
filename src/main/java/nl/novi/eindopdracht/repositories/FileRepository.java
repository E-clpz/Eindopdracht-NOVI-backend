package nl.novi.eindopdracht.repositories;

import nl.novi.eindopdracht.models.FileDocument;
import nl.novi.eindopdracht.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileDocument, Long> {
    FileDocument findByFileName(String fileName);
    List<FileDocument> findByRequest(Request request);
}