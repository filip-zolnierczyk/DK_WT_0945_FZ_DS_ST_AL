package org.agh.backend.service;

import org.agh.backend.dto.AppointmentCreateDto;
import org.agh.backend.dto.EmptyAppointmentDto;
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

    public List<EmptyAppointmentDto> getEmptyAppointments(Long dutyId) {
        Duty duty = dutyRepository.findById(dutyId).orElse(null);
        if (duty == null) {
            throw new IllegalArgumentException();
        }

        List<Appointment> takenAppointments = appointmentRepository.findAllByDuty(duty);
        List<LocalDateTime> availableHours = new ArrayList<>();
        for (LocalDateTime startHour = duty.getStart(); startHour.isBefore(duty.getFinish()); startHour = startHour.plusMinutes(Appointment.LENGTH)) {
            availableHours.add(startHour);
        }
        for (Appointment appointment : takenAppointments) {
            availableHours.remove(appointment.getStartTime());
        }

        List<EmptyAppointmentDto> emptyAppointments = new ArrayList<>();
        for (LocalDateTime startTime : availableHours) {
            emptyAppointments.add(
                    new EmptyAppointmentDto(
                            startTime,
                            startTime.plusMinutes(Appointment.LENGTH),
                            duty.getDoctor().getSpecialization().getName(),
                            duty.getDoctor().getName(),
                            duty.getOffice().getName()
                    )
            );
        }

        return emptyAppointments;
    }

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
            // TODO(And is free at the time)
        }

        // TODO(Check if slot is free and startTime-DutyStartTime is multiple of Appointment.LENGTH)

        Duty duty = dutyRepository.findById(appointmentCreateDto.getDutyId()).orElse(null);
        Patient patient = patientRepository.findById(appointmentCreateDto.getPatientId()).orElse(null);

        Appointment appointment = new Appointment(
            duty, patient, appointmentCreateDto.getStartTime()
        );
        appointmentRepository.save(appointment);
    }

}
