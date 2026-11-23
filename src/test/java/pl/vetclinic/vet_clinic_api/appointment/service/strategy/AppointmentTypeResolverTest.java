package pl.vetclinic.vet_clinic_api.appointment.service.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.vetclinic.vet_clinic_api.common.BadRequestException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentTypeResolverTest {

    @Mock
    private AppointmentTypeStrategy vaccinationStrategy;

    @Mock
    private AppointmentTypeStrategy surgeryStrategy;

    private AppointmentTypeResolver resolver;

    @BeforeEach
    void setUp() {
        when(vaccinationStrategy.getType()).thenReturn("VACCINATION");
        when(surgeryStrategy.getType()).thenReturn("SURGERY");
        resolver = new AppointmentTypeResolver(List.of(vaccinationStrategy, surgeryStrategy));
    }

    @Test
    void shouldReturnStrategyWhenTypeExists() {
        AppointmentTypeStrategy result = resolver.resolve("SURGERY");

        assertThat(result).isSameAs(surgeryStrategy);
    }

    @Test
    void shouldReturnStrategyIgnoringCaseAndWhitespace() {
        AppointmentTypeStrategy result = resolver.resolve("  vaccination ");

        assertThat(result).isSameAs(vaccinationStrategy);
    }

    @Test
    void shouldThrowBadRequestWhenTypeIsUnknown() {
        assertThatThrownBy(() -> resolver.resolve("DENTAL"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Unknown appointment type: DENTAL");
    }

    @Test
    void shouldThrowBadRequestWhenTypeIsNull() {
        assertThatThrownBy(() -> resolver.resolve(null))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void shouldFailWhenTwoStrategiesHaveSameType() {
        AppointmentTypeStrategy duplicate = mock(AppointmentTypeStrategy.class);
        when(duplicate.getType()).thenReturn("vaccination");
        List<AppointmentTypeStrategy> strategies = List.of(vaccinationStrategy, duplicate);

        assertThatThrownBy(() -> new AppointmentTypeResolver(strategies))
                .isInstanceOf(IllegalStateException.class);
    }
}