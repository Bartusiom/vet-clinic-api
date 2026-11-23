package pl.vetclinic.vet_clinic_api.appointment.service.strategy;

import java.time.Duration;

public interface AppointmentTypeStrategy {

    String getType();

    Duration getDuration();
}