package pl.vetclinic.vet_clinic_api.vet.service;

import pl.vetclinic.vet_clinic_api.vet.dto.CreateVetRequest;
import pl.vetclinic.vet_clinic_api.vet.dto.VetResponse;
import pl.vetclinic.vet_clinic_api.vet.model.Specialization;
import pl.vetclinic.vet_clinic_api.vet.model.Vet;

import java.util.List;

public interface VetService {

    VetResponse create(CreateVetRequest request);

    VetResponse getById(Long id);

    List<VetResponse> getAll();

    VetResponse update(Long id, CreateVetRequest request);

    void delete(Long id);

    List<VetResponse> findBySpecialization(Specialization specialization);

    Vet getEntityById(Long id);
}