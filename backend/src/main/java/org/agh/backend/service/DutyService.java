package org.agh.backend.service;

import org.agh.backend.model.Doctor;
import org.agh.backend.model.Duty;
import org.agh.backend.model.Office;
import org.agh.backend.repository.DutyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class DutyService {

    private final DutyRepository dutyRepository;

    public DutyService(DutyRepository dutyRepository) {
        this.dutyRepository = dutyRepository;
    }

    public Duty createDuty(Doctor doctor, Office office, LocalDateTime start, LocalDateTime finish) {
        if (start == null || finish == null) throw new IllegalArgumentException("Start and end cannot be null");
        if (!finish.isAfter(start)) throw new IllegalArgumentException("End must be after start");

        boolean doctorBusy = dutyRepository.existsByDoctorAndStartLessThanAndFinishGreaterThan(doctor, finish, start);
        if (doctorBusy) throw new IllegalStateException("Doctor already has a duty in this time range");

        boolean officeBusy = dutyRepository.existsByOfficeAndStartLessThanAndFinishGreaterThan(office, finish, start);
        if (officeBusy) throw new IllegalStateException("Office is already occupied in this time range");

        Duty duty = new Duty();
        duty.setDoctor(doctor);
        duty.setOffice(office);
        duty.setStart(start);
        duty.setFinish(finish);

        return dutyRepository.save(duty);
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

