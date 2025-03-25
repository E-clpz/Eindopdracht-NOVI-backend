package nl.novi.eindopdracht.services;


import nl.novi.eindopdracht.dtos.RequestDto;
import nl.novi.eindopdracht.mappers.RequestMapper;
import nl.novi.eindopdracht.models.Category;
import nl.novi.eindopdracht.models.Request;
import nl.novi.eindopdracht.models.Role;
import nl.novi.eindopdracht.models.User;
import nl.novi.eindopdracht.repositories.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTests {

    @InjectMocks
    private RequestService requestService;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private RequestMapper requestMapper;

    @Mock
    private User user;

    private List<Request> mockRequests;

    @BeforeEach
    void setUp() {
        Category boodschappen = new Category("Boodschappen");
        Category vervoer = new Category("Vervoer");
        Category gezelschap = new Category("Gezelschap");
        User requester = new User("Jan", "jan@example.com", "0612345678", "Amsterdam", "$2a$10$pVsrHQGv/0Imfu0ueOO0FuROd6f7T84cksaM.udVht6VEk91el1ke", Role.REQUESTER, 5);

        mockRequests = Arrays.asList(new Request("Hulp bij boodschappen", "Boodschappen doen voor een oudere persoon", boodschappen, "Gesloten", "Amsterdam", requester, LocalDate.parse("2025-01-01"), "163.jpeg"), new Request("Vervoer naar dokter", "Hulp nodig voor vervoer naar de dokter", vervoer, "Gesloten", "Rotterdam", requester, LocalDate.parse("2025-02-01"), null), new Request("Vervoer naar ziekenhuis", "Ik heb een lift nodig naar het ziekenhuis", gezelschap, "Gesloten", "Utrecht", requester, LocalDate.parse("2025-03-10"), null));
    }

    @Test
    void testGetAllRequestForHelpers() {
        when(requestRepository.findAll()).thenReturn(mockRequests);

        List<RequestDto> result = requestService.getAllRequestsForHelpers(null, null, "asc");

        assertEquals(3, result.size());
    }


}
