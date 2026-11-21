package pl.vetclinic.vet_clinic_api.appointment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.vetclinic.vet_clinic_api.appointment.dto.AppointmentResponse;
import pl.vetclinic.vet_clinic_api.appointment.dto.BookAppointmentRequest;
import pl.vetclinic.vet_clinic_api.appointment.mapper.AppointmentMapper;
import pl.vetclinic.vet_clinic_api.appointment.model.Appointment;
import pl.vetclinic.vet_clinic_api.appointment.model.AppointmentStatus;
import pl.vetclinic.vet_clinic_api.appointment.repository.AppointmentRepository;
import pl.vetclinic.vet_clinic_api.appointment.service.strategy.AppointmentTypeResolver;
import pl.vetclinic.vet_clinic_api.appointment.service.strategy.AppointmentTypeStrategy;
import pl.vetclinic.vet_clinic_api.common.ConflictException;
import pl.vetclinic.vet_clinic_api.common.NotFoundException;
import pl.vetclinic.vet_clinic_api.pet.model.Pet;
import pl.vetclinic.vet_clinic_api.pet.service.PetService;
import pl.vetclinic.vet_clinic_api.vet.model.Vet;
import pl.vetclinic.vet_clinic_api.vet.service.VetService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentTypeResolver typeResolver;
    private final AppointmentConflictValidator conflictValidator;
    private final AppointmentMapper appointmentMapper;
    private final PetService petService;
    private final VetService vetService;

    @Override
    @Transactional
    public AppointmentResponse book(BookAppointmentRequest request) {
        AppointmentTypeStrategy strategy = typeResolver.resolve(request.type());
        Pet pet = petService.getEntityById(request.petId());
        Vet vet = vetService.getEntityById(request.vetId());
        LocalDateTime endTime = request.startTime().plus(strategy.getDuration());

        conflictValidator.validateVetAvailability(vet.getId(), request.startTime(), endTime);

        Appointment appointment = Appointment.builder()
                .pet(pet)
                .vet(vet)
                .type(strategy.getType())
                .startTime(request.startTime())
                .endTime(endTime)
                .build();
        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional
    public AppointmentResponse cancel(Long id) {
        Appointment appointment = getAppointment(id);
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new ConflictException("Only scheduled appointments can be cancelled");
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
        return appointmentMapper.toResponse(appointment);
    }

    @Override
    public List<AppointmentResponse> findByVet(Long vetId) {
        ensureVetExists(vetId);
        return appointmentRepository.findAllByVetIdOrderByStartTimeAsc(vetId).stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    private Appointment getAppointment(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found: " + id));
    }

    private void ensureVetExists(Long vetId) {
        vetService.getEntityById(vetId);
    }
}