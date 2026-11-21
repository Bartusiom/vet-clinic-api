package pl.vetclinic.vet_clinic_api.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.vetclinic.vet_clinic_api.pet.model.Pet;
import pl.vetclinic.vet_clinic_api.pet.model.Species;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findBySpecies(Species species);
}
