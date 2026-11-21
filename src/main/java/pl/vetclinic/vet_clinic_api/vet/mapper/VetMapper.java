package pl.vetclinic.vet_clinic_api.vet.mapper;

import org.springframework.stereotype.Component;
import pl.vetclinic.vet_clinic_api.vet.dto.CreateVetRequest;
import pl.vetclinic.vet_clinic_api.vet.dto.VetResponse;
import pl.vetclinic.vet_clinic_api.vet.model.Vet;

@Component
public class VetMapper {

    public Vet toEntity(CreateVetRequest request) {
        Vet vet = new Vet();
        updateEntity(vet, request);
        return vet;
    }

    public void updateEntity(Vet vet, CreateVetRequest request) {
        vet.setFirstName(request.firstName());
        vet.setLastName(request.lastName());
        vet.setSpecialization(request.specialization());
    }

    public VetResponse toResponse(Vet vet) {
        return new VetResponse(
                vet.getId(),
                vet.getFirstName(),
                vet.getLastName(),
                vet.getFirstName() + " " + vet.getLastName(),
                vet.getSpecialization()
        );
    }
}