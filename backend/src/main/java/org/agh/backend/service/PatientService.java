package org.agh.backend.service;

import org.agh.backend.dto.AppointmentListDto;
import org.agh.backend.dto.PatientCreateDto;
import org.agh.backend.dto.PatientDto;
import org.agh.backend.model.Appointment;
import org.agh.backend.model.Patient;
import org.agh.backend.repository.PatientRepository;
import org.springframework.http.ResponseEntity;
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
     * @return list of PatientDto that representing all patients
     */
    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(PatientDto::new)
                .toList();
    }

    /**
     * Retrieves a patient by ID
     * @param id the ID of the patient
     * @return patientDto representing the patient
     */
    public PatientDto getPatientById(Long id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if  (patient == null) {
            throw new IllegalArgumentException("Patient with id " + id + " does not exist");
        }
        return new PatientDto(patient);
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
        Patient patient = patientRepository.findById(id).orElse(null);

        if (patient == null) {
            return false;
        }
        if (!patient.getAppointments().isEmpty()) {
            throw new IllegalStateException("Patient has associated appointments");
        }
        patientRepository.delete(patient);
        return true;
    }

    public List<AppointmentListDto> getAppointmentListByPatientId(Long id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient == null) {
            throw new IllegalArgumentException("Patient with id " + id + " does not exist");
        }

        return patient
                .getAppointments()
                .stream()
                .map(AppointmentListDto::new)
                .toList();
    }
}
