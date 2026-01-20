package org.agh.backend.service;

import org.agh.backend.dto.DutyCreateDto;
import org.agh.backend.dto.DutyDto;
import org.agh.backend.model.Appointment;
import org.agh.backend.model.Doctor;
import org.agh.backend.model.Duty;
import org.agh.backend.model.Office;
import org.agh.backend.repository.AppointmentRepository;
import org.agh.backend.repository.DoctorRepository;
import org.agh.backend.repository.DutyRepository;
import org.agh.backend.repository.OfficeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DutyService {

    private final DutyRepository dutyRepository;
    private final DoctorRepository doctorRepository;
    private final OfficeRepository officeRepository;
    private final AppointmentRepository appointmentRepository;

    public DutyService(
            DutyRepository dutyRepository,
            DoctorRepository doctorRepository,
            OfficeRepository officeRepository,
            AppointmentRepository appointmentRepository
    ) {
        this.dutyRepository = dutyRepository;
        this.doctorRepository = doctorRepository;
        this.officeRepository = officeRepository;
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Adds a new duty
     * @param dutyCreateDto includes data identifying doctor and office; specifies start and end times
     * @return dutyDto if duty created successfully
     * @throws IllegalArgumentException if input is incorrect
     * @throws IllegalStateException if doctor/office is busy and cannot be present in duty to be created
     */
    public DutyDto addDuty(DutyCreateDto dutyCreateDto) {
        if (dutyCreateDto.getStart() == null || dutyCreateDto.getFinish() == null) {
            throw new IllegalArgumentException("Start and end cannot be null");
        }
        if (!dutyCreateDto.getFinish().isAfter(dutyCreateDto.getStart())) {
            throw new IllegalArgumentException("End must be after start");
        }

        int duration = dutyCreateDto.getFinish().getMinute() - dutyCreateDto.getStart().getMinute();
        if (duration % Appointment.LENGTH != 0) {
            throw new IllegalArgumentException("Duration must be a multiple of " + Appointment.LENGTH);
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

    /**
     * Deletes a duty by its id
     * @param dutyId the ID of the duty to be deleted
     * @throws IllegalStateException if duty does not exist
     */
    public boolean deleteDuty(Long dutyId) {
        Duty duty = dutyRepository.findById(dutyId).orElse(null);
        if (duty == null) {
            return false;
        }
        if (!duty.getAppointments().isEmpty()) {
            throw new IllegalStateException("Duty has scheduled appointments");
        }
        dutyRepository.deleteById(dutyId);
        return true;
    }

    /**
     * Deletes all duties
     * @throws IllegalStateException if any duty has scheduled appointment
     */
    public void deleteAllDuties() {
        if (!appointmentRepository.findAll().isEmpty()) {
            throw new IllegalStateException("Some duties have scheduled appointments");
        }
        dutyRepository.deleteAll();
    }
}

