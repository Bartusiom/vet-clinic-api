package pl.vetclinic.vet_clinic_api.appointment.mapper;

import org.springframework.stereotype.Component;
import pl.vetclinic.vet_clinic_api.appointment.dto.AppointmentResponse;
import pl.vetclinic.vet_clinic_api.appointment.model.Appointment;
import pl.vetclinic.vet_clinic_api.vet.model.Vet;

@Component
public class AppointmentMapper {

    public AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPet().getId(),
                appointment.getPet().getName(),
                appointment.getVet().getId(),
                fullName(appointment.getVet()),
                appointment.getType(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getStatus()
        );
    }

    private String fullName(Vet vet) {
        return vet.getFirstName() + " " + vet.getLastName();
    }
}