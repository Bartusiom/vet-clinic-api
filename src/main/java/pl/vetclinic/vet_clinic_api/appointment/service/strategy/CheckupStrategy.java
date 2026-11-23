package pl.vetclinic.vet_clinic_api.appointment.service.strategy;

import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CheckupStrategy implements AppointmentTypeStrategy {

    @Override
    public String getType() {
        return "CHECKUP";
    }

    @Override
    public Duration getDuration() {
        return Duration.ofMinutes(30);
    }
}