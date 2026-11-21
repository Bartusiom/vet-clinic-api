package pl.vetclinic.vet_clinic_api.pet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.vetclinic.vet_clinic_api.common.NotFoundException;
import pl.vetclinic.vet_clinic_api.pet.dto.PetRequest;
import pl.vetclinic.vet_clinic_api.pet.dto.PetResponse;
import pl.vetclinic.vet_clinic_api.pet.mapper.PetMapper;
import pl.vetclinic.vet_clinic_api.pet.model.Pet;
import pl.vetclinic.vet_clinic_api.pet.model.Species;
import pl.vetclinic.vet_clinic_api.pet.repository.PetRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;

    @Override
    @Transactional
    public PetResponse create(PetRequest request) {
        Pet saved = petRepository.save(petMapper.toEntity(request));
        return petMapper.toResponse(saved);
    }

    @Override
    public PetResponse getById(Long id) {
        return petMapper.toResponse(getEntityById(id));
    }

    @Override
    public List<PetResponse> getAll() {
        return petRepository.findAll().stream()
                .map(petMapper::toResponse)
                .toList();
    }

    @Override
    public List<PetResponse> findBySpecies(Species species) {
        return petRepository.findBySpecies(species).stream()
                .map(petMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public PetResponse update(Long id, PetRequest request) {
        Pet pet = getEntityById(id);
        petMapper.updateEntity(pet, request);
        return petMapper.toResponse(petRepository.save(pet));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!petRepository.existsById(id)) {
            throw new NotFoundException("Pet with id " + id + " not found");
        }
        petRepository.deleteById(id);
    }

    @Override
    public Pet getEntityById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pet with id " + id + " not found"));
    }
}
