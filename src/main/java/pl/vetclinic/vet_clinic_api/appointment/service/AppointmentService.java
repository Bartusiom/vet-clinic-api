package pl.vetclinic.vet_clinic_api.appointment.service;

import pl.vetclinic.vet_clinic_api.appointment.dto.AppointmentResponse;
import pl.vetclinic.vet_clinic_api.appointment.dto.BookAppointmentRequest;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse book(BookAppointmentRequest request);

    AppointmentResponse cancel(Long id);

    List<AppointmentResponse> findByVet(Long vetId);
}