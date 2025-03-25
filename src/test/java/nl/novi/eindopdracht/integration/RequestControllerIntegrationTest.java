package nl.novi.eindopdracht.integration;

import nl.novi.eindopdracht.models.Category;
import nl.novi.eindopdracht.models.Request;
import nl.novi.eindopdracht.models.Role;
import nl.novi.eindopdracht.models.User;
import nl.novi.eindopdracht.repositories.CategoryRepository;
import nl.novi.eindopdracht.repositories.RequestRepository;
import nl.novi.eindopdracht.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "helper", roles = {"HELPER"})
class RequestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        requestRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        User requester = userRepository.save(new User("requester", "requester@example.com", "0612345678", "Amsterdam", "password", Role.REQUESTER, null));

        Category category1 = categoryRepository.save(new Category("Boodschappen"));
        Category category2 = categoryRepository.save(new Category("Vervoer"));
        Category category3 = categoryRepository.save(new Category("Gezelschap"));
        Category category4 = categoryRepository.save(new Category("Overig"));

        List<Request> testRequests = List.of(
                new Request("Hulp bij boodschappen", "Boodschappen doen voor een oudere persoon", category1, "Gesloten", "Amsterdam", requester, LocalDate.of(2025, 1, 1), null),
                new Request("Vervoer naar dokter", "Hulp nodig voor vervoer naar de dokter", category2, "Gesloten", "Rotterdam", requester, LocalDate.of(2025, 2, 1), null),
                new Request("Vervoer naar ziekenhuis", "Ik heb een lift nodig naar het ziekenhuis", category3, "Geslotenn", "Utrecht", requester, LocalDate.of(2025, 3, 10), null),
                new Request("Gezelschap voor wandeling", "Ik zoek iemand voor een gezellige wandeling in het park", category3, "Geaccepteerd", "Groningen", requester, LocalDate.of(2025, 4, 1), null),
                new Request("Boodschappen doen voor ouderen", "Ik help graag ouderen met hun wekelijkse boodschappen", category1, "Open", "Leiden", requester, LocalDate.of(2025, 4, 5), null),
                new Request("Vervoer naar ziekenhuis", "Ik heb iemand nodig die me naar het ziekenhuis kan brengen", category2, "Geaccepteerd", "Den Haag", requester, LocalDate.of(2025, 4, 10), null),
                new Request("Hulp met tuinieren", "Ik zoek hulp voor tuinonderhoud, zoals gras maaien en bloemen planten", category4, "Open", "Maastricht", requester, LocalDate.of(2025, 4, 15), null),
                new Request("Gezelschap bij een film", "Ik wil graag iemand uitnodigen om samen een film te kijken", category3, "Open", "Eindhoven", requester, LocalDate.of(2025, 4, 20), null)
        );
        requestRepository.saveAll(testRequests);
    }

    @Test
    void testGetAllRequests() throws Exception {
        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX0hFTFBFUiJdLCJ1c2VySWQiOjIsImF1ZCI6ImVpbmRvcGRyYWNodC1hcGkuY29tIiwic3ViIjoiaGVscGVyIiwiaWF0IjoxNzQyNjM1MjU1LCJleHAiOjE3NDM0OTkyNTV9.lpWqz3eM4fHDJg147A2quX-yerEXpVGABDI31L8gh5Y";

        mockMvc.perform(get("http://localhost:8080/api/requests")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(8)))
                .andExpect(jsonPath("$[0].title").value("Hulp bij boodschappen"))
                .andExpect(jsonPath("$[1].title").value("Vervoer naar dokter"))
                .andExpect(jsonPath("$[2].title").value("Vervoer naar ziekenhuis"))
                .andExpect(jsonPath("$[3].title").value("Gezelschap voor wandeling"))
                .andExpect(jsonPath("$[4].title").value("Boodschappen doen voor ouderen"))
                .andExpect(jsonPath("$[5].title").value("Vervoer naar ziekenhuis"))
                .andExpect(jsonPath("$[6].title").value("Hulp met tuinieren"))
                .andExpect(jsonPath("$[7].title").value("Gezelschap bij een film"));
    }
}
