package pl.vetclinic.vet_clinic_api.pet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pl.vetclinic.vet_clinic_api.pet.model.Species;

import java.time.LocalDate;

public record PetRequest(
        @NotBlank @Size(max = 50) String name,
        @NotNull Species species,
        @Past LocalDate birthDate,
        @NotBlank @Size(max = 100) String ownerName,
        @NotBlank @Pattern(regexp = "^(\\+48)?\\d{9}$", message = "must be 9 digits, optionally with +48")
        String ownerPhone
) {
}
