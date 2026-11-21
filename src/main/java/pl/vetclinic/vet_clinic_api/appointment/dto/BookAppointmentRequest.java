package pl.vetclinic.vet_clinic_api.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BookAppointmentRequest(
        @NotNull Long petId,
        @NotNull Long vetId,
        @NotNull @Future LocalDateTime startTime,
        @NotBlank String type
) {
}