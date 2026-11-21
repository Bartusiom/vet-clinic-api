package pl.vetclinic.vet_clinic_api.pet.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.vetclinic.vet_clinic_api.common.NotFoundException;
import pl.vetclinic.vet_clinic_api.pet.dto.PetRequest;
import pl.vetclinic.vet_clinic_api.pet.dto.PetResponse;
import pl.vetclinic.vet_clinic_api.pet.mapper.PetMapper;
import pl.vetclinic.vet_clinic_api.pet.model.Pet;
import pl.vetclinic.vet_clinic_api.pet.model.Species;
import pl.vetclinic.vet_clinic_api.pet.repository.PetRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @Spy
    private PetMapper petMapper;

    @InjectMocks
    private PetServiceImpl petService;

    @Test
    void shouldReturnPetWhenIdExists() {
        Pet pet = samplePet(1L, "Burek", Species.DOG);
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        PetResponse result = petService.getById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Burek");
    }

    @Test
    void shouldThrowNotFoundWhenPetDoesNotExist() {
        when(petRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> petService.getById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldSaveNewPet() {
        PetRequest request = new PetRequest(
                "Mruczek", Species.CAT, LocalDate.of(2021, 3, 1), "Anna Nowak", "987654321");
        when(petRepository.save(any(Pet.class)))
                .thenAnswer(invocation -> {
                    Pet saved = invocation.getArgument(0);
                    saved.setId(5L);
                    return saved;
                });

        PetResponse result = petService.create(request);

        assertThat(result.id()).isEqualTo(5L);
        assertThat(result.species()).isEqualTo(Species.CAT);
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void shouldReturnOnlyPetsOfGivenSpecies() {
        when(petRepository.findBySpecies(Species.DOG))
                .thenReturn(List.of(samplePet(1L, "Burek", Species.DOG)));

        List<PetResponse> result = petService.findBySpecies(Species.DOG);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().species()).isEqualTo(Species.DOG);
    }

    @Test
    void shouldNotDeleteWhenPetDoesNotExist() {
        when(petRepository.existsById(42L)).thenReturn(false);

        assertThatThrownBy(() -> petService.delete(42L))
                .isInstanceOf(NotFoundException.class);
        verify(petRepository, never()).deleteById(any());
    }

    private Pet samplePet(Long id, String name, Species species) {
        return new Pet(id, name, species, LocalDate.of(2020, 5, 10), "Jan Kowalski", "123456789");
    }
}
