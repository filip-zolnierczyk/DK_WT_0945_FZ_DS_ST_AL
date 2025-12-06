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

    public List<DoctorDto> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();

        return doctors.stream()
                .map(DoctorDto::new)
                .toList();
    }

    public DoctorDetailedDto getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        if (doctor == null) {
            return null;
        }
        return new DoctorDetailedDto(doctor);
    }

    public DoctorDetailedDto getDoctorByPesel(String pesel) {
        Doctor doctor = doctorRepository.findByPesel(pesel);
        if (doctor == null) {
            return null;
        }
        return new DoctorDetailedDto(doctor);
    }

    public boolean deleteDoctorById(Long id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            return true;
        }
        return false;
    }

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
