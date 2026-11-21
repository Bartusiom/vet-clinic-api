package pl.vetclinic.vet_clinic_api.pet.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.vetclinic.vet_clinic_api.pet.dto.PetRequest;
import pl.vetclinic.vet_clinic_api.pet.dto.PetResponse;
import pl.vetclinic.vet_clinic_api.pet.model.Species;
import pl.vetclinic.vet_clinic_api.pet.service.PetService;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping
    public List<PetResponse> getPets(@RequestParam(required = false) Species species) {
        if (species != null) {
            return petService.findBySpecies(species);
        }
        return petService.getAll();
    }

    @GetMapping("/{id}")
    public PetResponse getPet(@PathVariable Long id) {
        return petService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetResponse createPet(@Valid @RequestBody PetRequest request) {
        return petService.create(request);
    }

    @PutMapping("/{id}")
    public PetResponse updatePet(@PathVariable Long id, @Valid @RequestBody PetRequest request) {
        return petService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePet(@PathVariable Long id) {
        petService.delete(id);
    }
}
