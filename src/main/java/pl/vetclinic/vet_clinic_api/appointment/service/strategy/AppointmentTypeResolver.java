package pl.vetclinic.vet_clinic_api.appointment.service.strategy;

import org.springframework.stereotype.Component;
import pl.vetclinic.vet_clinic_api.common.BadRequestException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AppointmentTypeResolver {

    private final Map<String, AppointmentTypeStrategy> strategiesByType;

    public AppointmentTypeResolver(List<AppointmentTypeStrategy> strategies) {
        this.strategiesByType = strategies.stream()
                .collect(Collectors.toUnmodifiableMap(
                        strategy -> normalize(strategy.getType()),
                        Function.identity()));
    }

    public AppointmentTypeStrategy resolve(String type) {
        return Optional.ofNullable(type)
                .map(AppointmentTypeResolver::normalize)
                .map(strategiesByType::get)
                .orElseThrow(() -> new BadRequestException("Unknown appointment type: " + type));
    }

    private static String normalize(String type) {
        return type.trim().toUpperCase(Locale.ROOT);
    }
}