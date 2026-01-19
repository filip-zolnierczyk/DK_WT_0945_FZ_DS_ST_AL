package org.agh.backend.service;

import jakarta.transaction.Transactional;
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
        Duty duty = dutyRepository.findById(dutyId)
                .orElseThrow(() -> new IllegalArgumentException("Duty not found"));

        List<Appointment> takenAppointments = appointmentRepository.findAllByDuty(duty);
        List<AppointmentListDto> appointmentList = new ArrayList<>();

        for (LocalDateTime startHour = duty.getStart();
             startHour.isBefore(duty.getFinish());
             startHour = startHour.plusMinutes(Appointment.LENGTH)) {

            final LocalDateTime currentHour = startHour;

            Appointment matchingAppointment = takenAppointments.stream()
                    .filter(a -> a.getStartTime().equals(currentHour))
                    .findFirst()
                    .orElse(null);

            if (matchingAppointment != null) {
                appointmentList.add(new AppointmentListDto(matchingAppointment));
            } else {
                appointmentList.add(new AppointmentListDto(
                        null,
                        currentHour,
                        currentHour.plusMinutes(Appointment.LENGTH),
                        duty.getDoctor().getSpecialization().getName(),
                        duty.getDoctor().getName(),
                        duty.getOffice().getName(),
                        false
                ));
            }
        }

        return appointmentList;
    }

    /**
     * Adds an appointment
     * @param appointmentCreateDto dto representing appointment to be added
     * @throws IllegalArgumentException if given parameters representing null
     * @throws IllegalStateException if patient is busy or slot is occupied at given time
     */
    @Transactional
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

        Duty duty = dutyRepository.findById(appointmentCreateDto.getDutyId()).orElse(null);

        // Check if slot is free
        if (appointmentRepository.existsByDutyAndStartTime(duty, appointmentCreateDto.getStartTime())) {
            throw new IllegalStateException("Slot is occupied");
        }

        Patient patient = patientRepository.findById(appointmentCreateDto.getPatientId()).orElse(null);

        if (appointmentRepository.existsByPatientAndStartTime(patient, appointmentCreateDto.getStartTime())) {
            throw new IllegalStateException("Patient is busy");
        }

        Appointment appointment = new Appointment(
            duty, patient, appointmentCreateDto.getStartTime()
        );
        appointmentRepository.save(appointment);
    }

    /**
     * Cancels an appointment
     * @param id The ID of the appointment to cancel
     * @throws IllegalArgumentException if appointment does not exist
     */
    public void cancelAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new IllegalArgumentException();
        }
        appointmentRepository.deleteById(id);
    }

    @Transactional
    public List<AppointmentListDto> getAppointmentListByPatientId(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new IllegalArgumentException("Patient with id " + id + " does not exist");
        }

        List<Appointment> appointments = appointmentRepository.findAllByPatientId(id);

        return appointments.stream()
                .map(AppointmentListDto::new)
                .toList();
    }

}
