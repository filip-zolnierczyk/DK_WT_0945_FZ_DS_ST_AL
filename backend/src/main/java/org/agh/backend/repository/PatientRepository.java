package org.agh.backend.repository;

import org.agh.backend.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByPesel(String pesel);
    Patient findByPesel(String pesel);
    boolean existsById(Long patientId);
}
