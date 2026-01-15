package org.agh.backend.dto;

import lombok.Getter;
import org.agh.backend.model.Appointment;

import java.time.LocalDateTime;

@Getter
public class EmptyAppointmentDto {
    private final LocalDateTime startTime;
    private final LocalDateTime finishTime;
    private final String specializationName;
    private final String doctorName;
    private final String officeName;

    public EmptyAppointmentDto(LocalDateTime startTime,
                               LocalDateTime finishTime,
                               String specializationName,
                               String doctorName,
                               String officeName) {
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.specializationName = specializationName;
        this.doctorName = doctorName;
        this.officeName = officeName;
    }

    public EmptyAppointmentDto(Appointment appointment) {
        this.startTime = appointment.getStartTime();
        this.finishTime = appointment.getFinishTime();
        this.specializationName = appointment.getDuty().getDoctor().getSpecialization().getName();
        this.doctorName = appointment.getDuty().getDoctor().getName();
        this.officeName = appointment.getDuty().getOffice().getName();
    }
}
