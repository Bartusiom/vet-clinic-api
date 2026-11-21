package pl.vetclinic.vet_clinic_api.vet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.vetclinic.vet_clinic_api.vet.model.Specialization;
import pl.vetclinic.vet_clinic_api.vet.model.Vet;

import java.util.List;

public interface VetRepository extends JpaRepository<Vet, Long> {

    List<Vet> findBySpecialization(Specialization specialization);
}