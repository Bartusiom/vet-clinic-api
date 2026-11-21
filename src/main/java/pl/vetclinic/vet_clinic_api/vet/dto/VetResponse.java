package pl.vetclinic.vet_clinic_api.vet.dto;

import pl.vetclinic.vet_clinic_api.vet.model.Specialization;

public record VetResponse(
        Long id,
        String firstName,
        String lastName,
        String fullName,
        Specialization specialization
) {
}