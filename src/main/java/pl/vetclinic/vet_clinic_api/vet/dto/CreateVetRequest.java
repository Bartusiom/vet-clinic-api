package pl.vetclinic.vet_clinic_api.vet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.vetclinic.vet_clinic_api.vet.model.Specialization;

public record CreateVetRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull Specialization specialization
) {
}