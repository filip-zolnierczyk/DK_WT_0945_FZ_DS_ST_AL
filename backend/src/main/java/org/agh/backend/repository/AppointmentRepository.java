package org.agh.backend.repository;

import org.agh.backend.model.Appointment;
import org.agh.backend.model.Duty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByDuty(Duty duty);
}
