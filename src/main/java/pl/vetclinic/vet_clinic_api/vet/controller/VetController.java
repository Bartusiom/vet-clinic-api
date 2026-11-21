package pl.vetclinic.vet_clinic_api.vet.controller;

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
import pl.vetclinic.vet_clinic_api.vet.dto.CreateVetRequest;
import pl.vetclinic.vet_clinic_api.vet.dto.VetResponse;
import pl.vetclinic.vet_clinic_api.vet.model.Specialization;
import pl.vetclinic.vet_clinic_api.vet.service.VetService;

import java.util.List;

@RestController
@RequestMapping("/api/vets")
@RequiredArgsConstructor
public class VetController {

    private final VetService vetService;

    @GetMapping
    public List<VetResponse> getAll(
            @RequestParam(name = "specialization", required = false)
            Specialization specialization
    ) {
        if (specialization != null) {
            return vetService.findBySpecialization(specialization);
        }
        return vetService.getAll();
    }

    @GetMapping("/{id}")
    public VetResponse getById(@PathVariable("id") Long id) {
        return vetService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VetResponse create(
            @Valid @RequestBody CreateVetRequest request
    ) {
        return vetService.create(request);
    }

    @PutMapping("/{id}")
    public VetResponse update(
            @PathVariable("id") Long id,
            @Valid @RequestBody CreateVetRequest request
    ) {
        return vetService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        vetService.delete(id);
    }
}