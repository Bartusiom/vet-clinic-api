package pl.vetclinic.vet_clinic_api.appointment.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.vetclinic.vet_clinic_api.appointment.model.Appointment;
import pl.vetclinic.vet_clinic_api.appointment.model.AppointmentStatus;
import pl.vetclinic.vet_clinic_api.appointment.repository.AppointmentRepository;
import pl.vetclinic.vet_clinic_api.common.ConflictException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentConflictValidatorTest {

    private static final Long VET_ID = 1L;
    private static final LocalDateTime START = LocalDateTime.of(2030, 1, 15, 10, 0);
    private static final LocalDateTime END = START.plusMinutes(30);

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentConflictValidator validator;

    @Test
    void shouldThrowConflictWhenScheduledAppointmentOverlaps() {
        givenOverlapping(appointmentWithStatus(AppointmentStatus.SCHEDULED));

        assertThatThrownBy(() -> validator.validateVetAvailability(VET_ID, START, END))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void shouldPassWhenNoAppointmentOverlaps() {
        givenOverlapping();

        assertThatCode(() -> validator.validateVetAvailability(VET_ID, START, END))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldPassWhenOverlappingAppointmentIsCancelled() {
        givenOverlapping(appointmentWithStatus(AppointmentStatus.CANCELLED));

        assertThatCode(() -> validator.validateVetAvailability(VET_ID, START, END))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldThrowConflictWhenAnyOverlappingAppointmentIsScheduled() {
        givenOverlapping(
                appointmentWithStatus(AppointmentStatus.CANCELLED),
                appointmentWithStatus(AppointmentStatus.SCHEDULED));

        assertThatThrownBy(() -> validator.validateVetAvailability(VET_ID, START, END))
                .isInstanceOf(ConflictException.class);
    }

    private void givenOverlapping(Appointment... appointments) {
        when(appointmentRepository.findOverlappingForVet(VET_ID, START, END))
                .thenReturn(List.of(appointments));
    }

    private Appointment appointmentWithStatus(AppointmentStatus status) {
        return Appointment.builder()
                .startTime(START)
                .endTime(END)
                .status(status)
                .build();
    }
}