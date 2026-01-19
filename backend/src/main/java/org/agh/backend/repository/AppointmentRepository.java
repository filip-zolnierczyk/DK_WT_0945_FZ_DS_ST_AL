package org.agh.backend.repository;

import org.agh.backend.model.Appointment;
import org.agh.backend.model.Duty;
import org.agh.backend.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByDuty(Duty duty);
    boolean existsByDutyAndStartTime(Duty duty, LocalDateTime startTime);
    boolean existsByPatientAndStartTime(Patient patient, LocalDateTime startTime);
}
