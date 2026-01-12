package org.agh.backend.service;

import org.agh.backend.dto.DutyCreateDto;
import org.agh.backend.dto.DutyDto;
import org.agh.backend.model.Doctor;
import org.agh.backend.model.Duty;
import org.agh.backend.model.Office;
import org.agh.backend.repository.DoctorRepository;
import org.agh.backend.repository.DutyRepository;
import org.agh.backend.repository.OfficeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class DutyService {

    private final DutyRepository dutyRepository;
    private final DoctorRepository doctorRepository;
    private final OfficeRepository officeRepository;

    public DutyService(
            DutyRepository dutyRepository,
            DoctorRepository doctorRepository,
            OfficeRepository officeRepository
    ) {
        this.dutyRepository = dutyRepository;
        this.doctorRepository = doctorRepository;
        this.officeRepository = officeRepository;
    }

    public DutyDto addDuty(DutyCreateDto dutyCreateDto) {
        if (dutyCreateDto.getStart() == null || dutyCreateDto.getFinish() == null) {
            throw new IllegalArgumentException("Start and end cannot be null");
        }
        if (!dutyCreateDto.getFinish().isAfter(dutyCreateDto.getStart())) {
            throw new IllegalArgumentException("End must be after start");
        }

        Doctor doctor = doctorRepository.findById(dutyCreateDto.getDoctorId())
                .orElse(null);

        Office office = officeRepository.findById(dutyCreateDto.getOfficeId())
                .orElse(null);

        if (doctor == null || office == null) {
            throw new IllegalArgumentException("Doctor/Office cannot be null");
        }

        boolean doctorBusy = dutyRepository
                .existsByDoctorAndStartLessThanAndFinishGreaterThan(
                        doctor,
                        dutyCreateDto.getFinish(),
                        dutyCreateDto.getStart()
                );
        if (doctorBusy) {
            throw new IllegalStateException("Doctor already has a duty in this time range");
        }

        boolean officeBusy = dutyRepository.
                existsByOfficeAndStartLessThanAndFinishGreaterThan(office, dutyCreateDto.getFinish(), dutyCreateDto.getStart());
        if (officeBusy) throw new IllegalStateException("Office is already occupied in this time range");

        Duty duty = new Duty();
        duty.setDoctor(doctor);
        duty.setOffice(office);
        duty.setStart(dutyCreateDto.getStart());
        duty.setFinish(dutyCreateDto.getFinish());

        return new DutyDto(dutyRepository.save(duty));
    }

    public void deleteDuty(Long dutyId) {
        if (!dutyRepository.existsById(dutyId)) {
            throw new IllegalStateException("Duty not found");
        }
        dutyRepository.deleteById(dutyId);
    }

    public void deleteAllDuties() {
        dutyRepository.deleteAll();
    }
}

