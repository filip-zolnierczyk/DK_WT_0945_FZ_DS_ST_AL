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

    public Duty createDuty(
            Doctor doctor,
            Office office,
            LocalDateTime start,
            LocalDateTime end
    ) {
        // Walidacja czasu
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end time cannot be null");
        }

        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        // Sprawdzenie kolizji lekarza
        boolean doctorBusy =
                dutyRepository.existsByDoctorAndStartLessThanAndEndGreaterThan(
                        doctor, end, start
                );

        if (doctorBusy) {
            throw new IllegalStateException("Doctor already has a duty in this time range");
        }

        // Sprawdzenie kolizji gabinetu
        boolean officeBusy =
                dutyRepository.existsByOfficeAndStartLessThanAndEndGreaterThan(
                        office, end, start
                );

        if (officeBusy) {
            throw new IllegalStateException("Office is already occupied in this time range");
        }

        // Utworzenie i zapis dyżuru
        Duty duty = new Duty();
        duty.setDoctor(doctor);
        duty.setOffice(office);
        duty.setStart(start);
        duty.setEnd(end);

        return dutyRepository.save(duty);
    }

    public void deleteDuty(Long dutyId) {
        dutyRepository.deleteById(dutyId);
    }
}
