package org.agh.backend.repository;

import org.agh.backend.model.Doctor;
import org.agh.backend.model.Duty;
import org.agh.backend.model.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DutyRepository extends JpaRepository<Duty, Long> {

    List<Duty> findByDoctorId(Long doctorId);

    boolean existsByDoctorAndStartLessThanAndFinishGreaterThan(
            Doctor doctor,
            LocalDateTime finish,
            LocalDateTime start
    );

    boolean existsByOfficeAndStartLessThanAndFinishGreaterThan(
            Office office,
            LocalDateTime finish,
            LocalDateTime start
    );


}
