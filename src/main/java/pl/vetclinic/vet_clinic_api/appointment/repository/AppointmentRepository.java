package pl.vetclinic.vet_clinic_api.appointment.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.vetclinic.vet_clinic_api.appointment.model.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
            SELECT a
            FROM Appointment a
            WHERE a.vet.id = :vetId
              AND a.startTime < :endTime
              AND a.endTime > :startTime
            """)
    List<Appointment> findOverlappingForVet(@Param("vetId") Long vetId,
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);

    @EntityGraph(attributePaths = {"pet", "vet"})
    List<Appointment> findAllByVetIdOrderByStartTimeAsc(Long vetId);
}