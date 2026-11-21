package pl.vetclinic.vet_clinic_api.pet.dto;

import pl.vetclinic.vet_clinic_api.pet.model.Species;

import java.time.LocalDate;

public record PetResponse(
        Long id,
        String name,
        Species species,
        LocalDate birthDate,
        String ownerName,
        String ownerPhone
) {
}
