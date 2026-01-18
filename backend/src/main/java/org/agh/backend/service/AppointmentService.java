package org.agh.backend.service;

import org.agh.backend.dto.AppointmentCreateDto;
import org.agh.backend.dto.AppointmentListDto;
import org.agh.backend.model.*;
import org.agh.backend.repository.AppointmentRepository;
import org.agh.backend.repository.DutyRepository;
import org.agh.backend.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DutyRepository dutyRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              DutyRepository dutyRepository,
                              PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.dutyRepository = dutyRepository;
        this.patientRepository = patientRepository;
    }

    /**
     * Retrieves list of appointments
     * @param dutyId the ID of the duty
     * @return list of AppointmentListDto representing appointments
     * @throws IllegalArgumentException if duty does not exist
     */
    public List<AppointmentListDto> getAppointmentListByDutyId(Long dutyId) {
        Duty duty = dutyRepository.findById(dutyId).orElse(null);
        if (duty == null) {
            throw new IllegalArgumentException();
        }

        List<Appointment> takenAppointments = appointmentRepository.findAllByDuty(duty);
        List<LocalDateTime> availableHours = new ArrayList<>();
        for (LocalDateTime startHour = duty.getStart(); startHour.isBefore(duty.getFinish()); startHour = startHour.plusMinutes(Appointment.LENGTH)) {
            availableHours.add(startHour);
        }
        List<AppointmentListDto> appointmentList = new ArrayList<>();

        // Adding taken appointments
        for (Appointment appointment : takenAppointments) {
            appointmentList.add(
                    new AppointmentListDto(appointment)
            );
        }

        // Adding empty appointments
        for (LocalDateTime startTime : availableHours) {
            appointmentList.add(
                    new AppointmentListDto(
                            startTime,
                            startTime.plusMinutes(Appointment.LENGTH),
                            duty.getDoctor().getSpecialization().getName(),
                            duty.getDoctor().getName(),
                            duty.getOffice().getName(),
                            true
                    )
            );
        }

        return appointmentList;
    }

    /**
     * Adds an appointment
     * @param appointmentCreateDto dto representing appointment to be added
     * @throws IllegalArgumentException if given parameters representing null
     * @throws IllegalStateException if patient is busy at given time
     */
    public void addAppointment(AppointmentCreateDto appointmentCreateDto) {
        if (appointmentCreateDto == null) {
            throw new IllegalArgumentException();
        }

        // Check duty
        if (!dutyRepository.existsById(appointmentCreateDto.getDutyId())) {
            throw new IllegalArgumentException();
        }

        // Check patient (if exists and is free)
        if (!patientRepository.existsById(appointmentCreateDto.getPatientId())) {
            throw new IllegalArgumentException();
        }
        // TODO(Check if patient is free, throws IllegalStateException)

        // TODO(Check if slot is free and startTime-DutyStartTime is multiple of Appointment.LENGTH)

        Duty duty = dutyRepository.findById(appointmentCreateDto.getDutyId()).orElse(null);
        Patient patient = patientRepository.findById(appointmentCreateDto.getPatientId()).orElse(null);

        Appointment appointment = new Appointment(
            duty, patient, appointmentCreateDto.getStartTime()
        );
        appointmentRepository.save(appointment);
    }

}
