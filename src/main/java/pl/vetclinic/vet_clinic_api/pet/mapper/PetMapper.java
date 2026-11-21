package pl.vetclinic.vet_clinic_api.pet.mapper;

import org.springframework.stereotype.Component;
import pl.vetclinic.vet_clinic_api.pet.dto.PetRequest;
import pl.vetclinic.vet_clinic_api.pet.dto.PetResponse;
import pl.vetclinic.vet_clinic_api.pet.model.Pet;

@Component
public class PetMapper {

    public Pet toEntity(PetRequest request) {
        Pet pet = new Pet();
        updateEntity(pet, request);
        return pet;
    }

    public void updateEntity(Pet pet, PetRequest request) {
        pet.setName(request.name());
        pet.setSpecies(request.species());
        pet.setBirthDate(request.birthDate());
        pet.setOwnerName(request.ownerName());
        pet.setOwnerPhone(request.ownerPhone());
    }

    public PetResponse toResponse(Pet pet) {
        return new PetResponse(
                pet.getId(),
                pet.getName(),
                pet.getSpecies(),
                pet.getBirthDate(),
                pet.getOwnerName(),
                pet.getOwnerPhone()
        );
    }
}
