package pl.vetclinic.vet_clinic_api.appointment.dto;

import pl.vetclinic.vet_clinic_api.appointment.model.AppointmentStatus;

import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        Long petId,
        String petName,
        Long vetId,
        String vetFullName,
        String type,
        LocalDateTime startTime,
        LocalDateTime endTime,
        AppointmentStatus status
) {
}