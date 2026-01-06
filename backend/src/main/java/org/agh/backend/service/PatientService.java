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

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

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

    public boolean deletePatient(Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
