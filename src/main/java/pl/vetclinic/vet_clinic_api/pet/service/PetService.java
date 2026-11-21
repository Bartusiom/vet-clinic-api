package pl.vetclinic.vet_clinic_api.pet.service;

import pl.vetclinic.vet_clinic_api.pet.dto.PetRequest;
import pl.vetclinic.vet_clinic_api.pet.dto.PetResponse;
import pl.vetclinic.vet_clinic_api.pet.model.Pet;
import pl.vetclinic.vet_clinic_api.pet.model.Species;

import java.util.List;

public interface PetService {

    PetResponse create(PetRequest request);

    PetResponse getById(Long id);

    List<PetResponse> getAll();

    List<PetResponse> findBySpecies(Species species);

    PetResponse update(Long id, PetRequest request);

    void delete(Long id);

    Pet getEntityById(Long id);
}
