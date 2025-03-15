package nl.novi.eindopdracht.repositories;

import nl.novi.eindopdracht.models.FileDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileDocument, Long> {
    FileDocument findByFileName(String fileName);
}