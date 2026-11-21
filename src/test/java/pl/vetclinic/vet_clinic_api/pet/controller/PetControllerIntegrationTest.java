package pl.vetclinic.vet_clinic_api.pet.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.vetclinic.vet_clinic_api.pet.model.Pet;
import pl.vetclinic.vet_clinic_api.pet.model.Species;
import pl.vetclinic.vet_clinic_api.pet.repository.PetRepository;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetRepository petRepository;

    @BeforeEach
    void cleanDatabase() {
        petRepository.deleteAll();
    }

    @Test
    void shouldCreatePet() throws Exception {
        String body = """
                {
                  "name": "Burek",
                  "species": "DOG",
                  "birthDate": "2020-05-10",
                  "ownerName": "Jan Kowalski",
                  "ownerPhone": "123456789"
                }
                """;

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Burek"))
                .andExpect(jsonPath("$.species").value("DOG"));
    }

    @Test
    void shouldReturnOnlyDogsWhenFilteringBySpecies() throws Exception {
        savePet("Burek", Species.DOG);
        savePet("Mruczek", Species.CAT);

        mockMvc.perform(get("/api/pets").param("species", "DOG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Burek"));
    }

    @Test
    void shouldReturn404WhenPetDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/pets/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldReturn400WhenNameIsBlank() throws Exception {
        String body = """
                {
                  "name": "",
                  "species": "DOG",
                  "ownerName": "Jan Kowalski",
                  "ownerPhone": "123456789"
                }
                """;

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.name").exists());
    }

    private void savePet(String name, Species species) {
        petRepository.save(new Pet(null, name, species, LocalDate.of(2020, 1, 1), "Jan Kowalski", "123456789"));
    }
}
