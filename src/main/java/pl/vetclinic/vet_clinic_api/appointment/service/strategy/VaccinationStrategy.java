package pl.vetclinic.vet_clinic_api.appointment.service.strategy;

import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class VaccinationStrategy implements AppointmentTypeStrategy {

    @Override
    public String getType() {
        return "VACCINATION";
    }

    @Override
    public Duration getDuration() {
        return Duration.ofMinutes(15);
    }
}