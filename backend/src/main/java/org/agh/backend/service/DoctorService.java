package org.agh.backend.service;

import org.agh.backend.dto.DoctorDetailedDto;
import org.agh.backend.dto.DoctorDto;
import org.agh.backend.model.Doctor;
import org.agh.backend.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
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

    public boolean deleteDoctorById(Long id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean addDoctor(Doctor doctor) {
        if (doctorRepository.existsByPesel(doctor.getPesel())) {
            return false;
        }
        doctorRepository.save(doctor);
        return true;
    }

}
