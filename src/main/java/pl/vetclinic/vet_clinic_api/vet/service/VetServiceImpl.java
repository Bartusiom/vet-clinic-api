package pl.vetclinic.vet_clinic_api.vet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.vetclinic.vet_clinic_api.common.NotFoundException;
import pl.vetclinic.vet_clinic_api.vet.dto.CreateVetRequest;
import pl.vetclinic.vet_clinic_api.vet.dto.VetResponse;
import pl.vetclinic.vet_clinic_api.vet.mapper.VetMapper;
import pl.vetclinic.vet_clinic_api.vet.model.Specialization;
import pl.vetclinic.vet_clinic_api.vet.model.Vet;
import pl.vetclinic.vet_clinic_api.vet.repository.VetRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VetServiceImpl implements VetService {

    private final VetRepository vetRepository;
    private final VetMapper vetMapper;

    @Override
    @Transactional
    public VetResponse create(CreateVetRequest request) {
        Vet vet = vetMapper.toEntity(request);
        Vet savedVet = vetRepository.save(vet);
        return vetMapper.toResponse(savedVet);
    }

    @Override
    public VetResponse getById(Long id) {
        return vetMapper.toResponse(getEntityById(id));
    }

    @Override
    public List<VetResponse> getAll() {
        return vetRepository.findAll()
                .stream()
                .map(vetMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public VetResponse update(Long id, CreateVetRequest request) {
        Vet vet = getEntityById(id);
        vetMapper.updateEntity(vet, request);
        Vet savedVet = vetRepository.save(vet);
        return vetMapper.toResponse(savedVet);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Vet vet = getEntityById(id);
        vetRepository.delete(vet);
    }

    @Override
    public List<VetResponse> findBySpecialization(
            Specialization specialization
    ) {
        return vetRepository.findBySpecialization(specialization)
                .stream()
                .map(vetMapper::toResponse)
                .toList();
    }

    @Override
    public Vet getEntityById(Long id) {
        return vetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Vet with id " + id + " not found"
                ));
    }
}