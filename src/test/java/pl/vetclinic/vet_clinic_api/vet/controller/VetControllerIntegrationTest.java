package pl.vetclinic.vet_clinic_api.vet.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pl.vetclinic.vet_clinic_api.vet.model.Specialization;
import pl.vetclinic.vet_clinic_api.vet.model.Vet;
import pl.vetclinic.vet_clinic_api.vet.repository.VetRepository;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class VetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VetRepository vetRepository;

    @Test
    void shouldCreateVetWhenRequestIsValid() throws Exception {
        mockMvc.perform(post("/api/vets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Anna",
                                  "lastName": "Nowak",
                                  "specialization": "SURGERY"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstName").value("Anna"))
                .andExpect(jsonPath("$.lastName").value("Nowak"))
                .andExpect(jsonPath("$.fullName").value("Anna Nowak"))
                .andExpect(jsonPath("$.specialization").value("SURGERY"));
    }

    @Test
    void shouldReturnOnlyVetsMatchingSpecialization() throws Exception {
        saveVet("Anna", "Nowak", Specialization.SURGERY);
        saveVet("Jan", "Kowalski", Specialization.GENERAL);
        saveVet("Ewa", "Lis", Specialization.SURGERY);

        mockMvc.perform(get("/api/vets")
                        .param("specialization", "SURGERY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].fullName", containsInAnyOrder(
                        "Anna Nowak",
                        "Ewa Lis"
                )))
                .andExpect(jsonPath("$[0].specialization").value("SURGERY"))
                .andExpect(jsonPath("$[1].specialization").value("SURGERY"));
    }

    @Test
    void shouldReturnNotFoundAfterDeletingVet() throws Exception {
        Vet vet = saveVet("Anna", "Nowak", Specialization.SURGERY);

        mockMvc.perform(get("/api/vets/{id}", vet.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Anna Nowak"));

        mockMvc.perform(delete("/api/vets/{id}", vet.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/vets/{id}", vet.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenLastNameIsMissing() throws Exception {
        mockMvc.perform(post("/api/vets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Anna",
                                  "specialization": "SURGERY"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    private Vet saveVet(
            String firstName,
            String lastName,
            Specialization specialization
    ) {
        Vet vet = new Vet();
        vet.setFirstName(firstName);
        vet.setLastName(lastName);
        vet.setSpecialization(specialization);
        return vetRepository.saveAndFlush(vet);
    }
}