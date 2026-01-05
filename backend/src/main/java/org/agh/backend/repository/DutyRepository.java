package org.agh.backend.repository;

import org.agh.backend.model.Doctor;
import org.agh.backend.model.Duty;
import org.agh.backend.model.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DutyRepository extends JpaRepository<Duty, Long> {

    boolean existsByDoctorAndStartLessThanAndEndGreaterThan(
            Doctor doctor,
            LocalDateTime end,
            LocalDateTime start
    );

    boolean existsByOfficeAndStartLessThanAndEndGreaterThan(
            Office office,
            LocalDateTime end,
            LocalDateTime start
    );
}
