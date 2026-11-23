package pl.vetclinic.vet_clinic_api.appointment.service.strategy;

import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class SurgeryStrategy implements AppointmentTypeStrategy {

    @Override
    public String getType() {
        return "SURGERY";
    }

    @Override
    public Duration getDuration() {
        return Duration.ofMinutes(120);
    }
}