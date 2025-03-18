package nl.novi.eindopdracht;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import nl.novi.eindopdracht.models.Category;
import nl.novi.eindopdracht.models.FileDocument;
import nl.novi.eindopdracht.models.Request;
import nl.novi.eindopdracht.models.User;
import nl.novi.eindopdracht.repositories.CategoryRepository;
import nl.novi.eindopdracht.repositories.FileRepository;
import nl.novi.eindopdracht.repositories.RequestRepository;
import nl.novi.eindopdracht.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FileUploadIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFileUploadAndLinkToRequest() throws Exception {

        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1JFUVVFU1RFUiJdLCJ1c2VySWQiOjEsImF1ZCI6ImVpbmRvcGRyYWNodC1hcGkuY29tIiwic3ViIjoicmVxdWVzdGVyIiwiaWF0IjoxNzQyMTU0NzM0LCJleHAiOjE3NDMwMTg3MzR9.sQaWuLa6m47pwoL8vYBfQ67doVw2SGqeByQo4N0Rmqc";

        Category category = categoryRepository.findById(1L).orElseThrow(() -> new RuntimeException("Category not found"));
        User requester = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found"));

        String fileUrl = "http://localhost:8080/downloadFromDB/testfile.jpg";

        Request request = new Request(
                "Test Hulpvraag",
                "Beschrijving van de test hulpvraag",
                category,
                "Open",
                "Amsterdam",
                requester,
                LocalDate.now(),
                "testfile.jpg"
        );

        requestRepository.save(request);

        Long requestId = request.getId();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "testfile.jpg",
                "image/jpeg",
                "Dit is een testbestand".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/single/uploadDb")
                        .file(file)
                        .param("requestId", String.valueOf(requestId))
                        .header("Authorization", token)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        List<FileDocument> files = fileRepository.findAll();
        assertFalse(files.isEmpty());

        FileDocument uploadedFile = files.get(0);
        assertEquals("testfile.jpg", uploadedFile.getFileName());
        assertEquals("image/jpeg", uploadedFile.getContentType());
        assertNotNull(uploadedFile.getFileData());
        assertEquals(requestId, uploadedFile.getRequest().getId());
    }
}
