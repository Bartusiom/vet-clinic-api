package pl.vetclinic.vet_clinic_api.vet.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.vetclinic.vet_clinic_api.common.NotFoundException;
import pl.vetclinic.vet_clinic_api.vet.dto.CreateVetRequest;
import pl.vetclinic.vet_clinic_api.vet.dto.VetResponse;
import pl.vetclinic.vet_clinic_api.vet.mapper.VetMapper;
import pl.vetclinic.vet_clinic_api.vet.model.Specialization;
import pl.vetclinic.vet_clinic_api.vet.model.Vet;
import pl.vetclinic.vet_clinic_api.vet.repository.VetRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VetServiceImplTest {

    @Mock
    private VetRepository vetRepository;

    private VetServiceImpl vetService;

    @BeforeEach
    void setUp() {
        vetService = new VetServiceImpl(vetRepository, new VetMapper());
    }

    @Test
    void shouldReturnVetsMatchingSpecialization() {
        Vet vet = new Vet();
        vet.setId(1L);
        vet.setFirstName("Anna");
        vet.setLastName("Nowak");
        vet.setSpecialization(Specialization.SURGERY);

        when(vetRepository.findBySpecialization(Specialization.SURGERY))
                .thenReturn(List.of(vet));

        List<VetResponse> result =
                vetService.findBySpecialization(Specialization.SURGERY);

        assertEquals(1, result.size());
        VetResponse response = result.getFirst();
        assertAll(
                () -> assertEquals(1L, response.id()),
                () -> assertEquals("Anna Nowak", response.fullName()),
                () -> assertEquals(
                        Specialization.SURGERY,
                        response.specialization()
                )
        );
        verify(vetRepository)
                .findBySpecialization(Specialization.SURGERY);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        when(vetRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> vetService.getById(99L)
        );
    }

    @Test
    void shouldUpdateVetWhenIdExists() {
        Vet vet = new Vet();
        vet.setId(1L);
        vet.setFirstName("Jan");
        vet.setLastName("Kowalski");
        vet.setSpecialization(Specialization.GENERAL);

        CreateVetRequest request = new CreateVetRequest(
                "Anna",
                "Nowak",
                Specialization.CARDIOLOGY
        );

        when(vetRepository.findById(1L))
                .thenReturn(Optional.of(vet));
        when(vetRepository.save(vet))
                .thenReturn(vet);

        VetResponse result = vetService.update(1L, request);

        assertAll(
                () -> assertEquals(1L, result.id()),
                () -> assertEquals("Anna", result.firstName()),
                () -> assertEquals("Nowak", result.lastName()),
                () -> assertEquals("Anna Nowak", result.fullName()),
                () -> assertEquals(
                        Specialization.CARDIOLOGY,
                        result.specialization()
                ),
                () -> assertEquals("Anna", vet.getFirstName()),
                () -> assertEquals("Nowak", vet.getLastName()),
                () -> assertEquals(
                        Specialization.CARDIOLOGY,
                        vet.getSpecialization()
                )
        );
        verify(vetRepository).save(vet);
    }
}