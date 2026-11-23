package pl.vetclinic.vet_clinic_api.appointment.controller;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.vetclinic.vet_clinic_api.appointment.dto.BookAppointmentRequest;
import pl.vetclinic.vet_clinic_api.appointment.repository.AppointmentRepository;
import pl.vetclinic.vet_clinic_api.pet.model.Pet;
import pl.vetclinic.vet_clinic_api.pet.model.Species;
import pl.vetclinic.vet_clinic_api.pet.repository.PetRepository;
import pl.vetclinic.vet_clinic_api.vet.model.Specialization;
import pl.vetclinic.vet_clinic_api.vet.model.Vet;
import pl.vetclinic.vet_clinic_api.vet.repository.VetRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AppointmentControllerIntegrationTest {

    private static final LocalDateTime START = LocalDateTime.now()
            .plusDays(1)
            .withHour(10)
            .truncatedTo(ChronoUnit.HOURS);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VetRepository vetRepository;

    private Long petId;
    private Long vetId;

    @BeforeEach
    void setUp() {
        petId = petRepository.save(createPet()).getId();
        vetId = vetRepository.save(createVet()).getId();
    }

    @AfterEach
    void tearDown() {
        appointmentRepository.deleteAll();
        petRepository.deleteAll();
        vetRepository.deleteAll();
    }

    @Test
    void shouldReturnCreatedWhenSlotIsFree() throws Exception {
        book(START)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.type").value("CHECKUP"))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    void shouldReturnConflictWhenSlotIsTaken() throws Exception {
        book(START).andExpect(status().isCreated());

        book(START.plusMinutes(15))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldAllowBookingAfterConflictingAppointmentIsCancelled() throws Exception {
        Long firstId = bookAndGetId(START);
        book(START).andExpect(status().isConflict());

        mockMvc.perform(patch("/api/appointments/{id}/cancel", firstId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        book(START).andExpect(status().isCreated());
    }

    private ResultActions book(LocalDateTime startTime) throws Exception {
        BookAppointmentRequest request = new BookAppointmentRequest(petId, vetId, startTime, "checkup");
        return mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    private Long bookAndGetId(LocalDateTime startTime) throws Exception {
        String body = book(startTime)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readTree(body).get("id").asLong();
    }

    private Pet createPet() {
        Pet pet = new Pet();
        pet.setName("Burek");
        pet.setSpecies(Species.DOG);
        pet.setOwnerName("Jan Kowalski");
        pet.setOwnerPhone("500600700");
        return pet;
    }

    private Vet createVet() {
        Vet vet = new Vet();
        vet.setFirstName("Anna");
        vet.setLastName("Nowak");
        vet.setSpecialization(Specialization.SURGERY);
        return vet;
    }
}