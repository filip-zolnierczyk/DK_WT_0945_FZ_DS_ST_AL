package org.agh.backend.dto;

import lombok.Getter;
import org.agh.backend.model.Appointment;

import java.time.LocalDateTime;

@Getter
public class AppointmentListDto {
    private final LocalDateTime startTime;
    private final LocalDateTime finishTime;
    private final String specializationName;
    private final String doctorName;
    private final String officeName;
    private final boolean occupied;

    public AppointmentListDto(LocalDateTime startTime,
                              LocalDateTime finishTime,
                              String specializationName,
                              String doctorName,
                              String officeName,
                              boolean occupied) {
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.specializationName = specializationName;
        this.doctorName = doctorName;
        this.officeName = officeName;
        this.occupied = occupied;
    }

    public AppointmentListDto(Appointment appointment) {
        this.startTime = appointment.getStartTime();
        this.finishTime = appointment.getFinishTime();
        this.specializationName = appointment.getDuty().getDoctor().getSpecialization().getName();
        this.doctorName = appointment.getDuty().getDoctor().getName();
        this.officeName = appointment.getDuty().getOffice().getName();
        this.occupied = true;
    }
}
