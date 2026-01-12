package org.agh.backend.service;

import org.agh.backend.dto.PatientCreateDto;
import org.agh.backend.model.Patient;
import org.agh.backend.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Retrieves all patients
     * @return list that includes all patients
     */
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    /**
     * Retrieves a patient by ID
     * @param id the ID of the patient
     * @return patient
     */
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    /**
     * Adds patient
     * @param dto representing patient's data
     * @return true if added successfully, false otherwise
     * @throws IllegalStateException if any field is null
     */
    public boolean addPatient(PatientCreateDto dto) {
        if (dto.getName() == null || dto.getSurname() == null || dto.getPesel() == null || dto.getAddress() == null) {
            throw new IllegalArgumentException("Fields cannot be null");
        }

        if (patientRepository.existsByPesel(dto.getPesel())) {
            return false;
        }

        Patient patient = new Patient(
                dto.getName(),
                dto.getSurname(),
                dto.getPesel(),
                dto.getAddress()
        );

        patientRepository.save(patient);
        return true;
    }

    /**
     * Deletes a patient by ID
     * @param id the ID of the patient to be deleted
     * @return true if deleted, false otherwise
     */
    public boolean deletePatient(Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
