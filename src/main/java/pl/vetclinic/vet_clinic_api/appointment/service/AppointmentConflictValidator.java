package pl.vetclinic.vet_clinic_api.appointment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.vetclinic.vet_clinic_api.appointment.model.Appointment;
import pl.vetclinic.vet_clinic_api.appointment.model.AppointmentStatus;
import pl.vetclinic.vet_clinic_api.appointment.repository.AppointmentRepository;
import pl.vetclinic.vet_clinic_api.common.ConflictException;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AppointmentConflictValidator {

    private final AppointmentRepository appointmentRepository;

    public void validateVetAvailability(Long vetId, LocalDateTime startTime, LocalDateTime endTime) {
        boolean hasConflict = appointmentRepository.findOverlappingForVet(vetId, startTime, endTime)
                .stream()
                .anyMatch(AppointmentConflictValidator::blocksTimeSlot);

        if (hasConflict) {
            throw new ConflictException(
                    "Vet %d already has an appointment between %s and %s".formatted(vetId, startTime, endTime));
        }
    }

    private static boolean blocksTimeSlot(Appointment appointment) {
        return appointment.getStatus() == AppointmentStatus.SCHEDULED;
    }
}