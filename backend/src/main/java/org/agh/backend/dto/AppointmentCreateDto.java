package org.agh.backend.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AppointmentCreateDto {
    private final Long dutyId;
    private final Long patientId;
    private final LocalDateTime startTime;

    public AppointmentCreateDto(Long dutyId, Long patientId, LocalDateTime startTime) {
        this.dutyId = dutyId;
        this.patientId = patientId;
        this.startTime = startTime;
    }
}
