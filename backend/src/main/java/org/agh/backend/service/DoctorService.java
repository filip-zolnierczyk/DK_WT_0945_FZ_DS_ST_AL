package org.agh.backend.service;

import org.agh.backend.dto.DoctorDetailedDto;
import org.agh.backend.dto.DoctorDto;
import org.agh.backend.model.Doctor;
import org.agh.backend.model.Specialization;
import org.agh.backend.repository.DoctorRepository;
import org.agh.backend.repository.SpecializationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final SpecializationRepository specializationRepository;

    public DoctorService(DoctorRepository doctorRepository, SpecializationRepository specializationRepository) {
        this.doctorRepository = doctorRepository;
        this.specializationRepository = specializationRepository;
    }

    /**
     * Retrieves all doctors.
     * @return a list of DoctorDto representing all doctors
     */
    public List<DoctorDto> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();

        return doctors.stream()
                .map(DoctorDto::new)
                .toList();
    }

    /**
     * Retrieves a doctor by their ID.
     * @param id the ID of the doctor
     * @return a DoctorDetailedDto representing the doctor, or null if not found
     */
    public DoctorDetailedDto getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        if (doctor == null) {
            return null;
        }
        return new DoctorDetailedDto(doctor);
    }

    /**
     * Retrieves a doctor by their PESEL number.
     * @param pesel the PESEL number of the doctor
     * @return a DoctorDetailedDto representing the doctor, or null if not found
     */
    public DoctorDetailedDto getDoctorByPesel(String pesel) {
        Doctor doctor = doctorRepository.findByPesel(pesel);
        if (doctor == null) {
            return null;
        }
        return new DoctorDetailedDto(doctor);
    }

    /**
     * Deletes a doctor by their ID.
     * @param id the ID of the doctor to delete
     * @return true if the doctor was deleted, false if not found
     */
    public boolean deleteDoctorById(Long id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Adds a new doctor if a doctor with the same PESEL does not already exist.
     * @param name the first name of the doctor
     * @param surname the last name of the doctor
     * @param pesel the PESEL number of the doctor
     * @param specializationName the specialization of the doctor
     * @param address the address of the doctor
     * @return true if the doctor was added, false if a doctor with the same PESEL already exists
     * @throws IllegalArgumentException if any argument is null
     */
    public boolean addDoctor(String name, String surname, String pesel, String specializationName, String address) {

        if (name == null || surname == null || pesel == null || specializationName == null || address == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        if (doctorRepository.existsByPesel(pesel)) {
            return false;
        }


        Specialization specialization = specializationRepository.findByName(specializationName);
        if (specialization == null) {
            specialization = specializationRepository.save(new Specialization(specializationName));
        }

        Doctor doctor = new Doctor(
                name,
                surname,
                pesel,
                specialization,
                address
        );

        doctorRepository.save(doctor);
        return true;
    }

}
