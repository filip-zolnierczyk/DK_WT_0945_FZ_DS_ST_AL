package org.agh.backend.dto;

import org.agh.backend.model.Duty;

import java.time.LocalDateTime;

public class DutyDto {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private String officeName;

    public DutyDto(Duty duty) {
        this.id = duty.getId();
        this.start = duty.getStart();
        this.end = duty.getEnd();
        this.officeName = duty.getOffice().getName();
    }
}
