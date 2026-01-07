package org.agh.backend.dto;

import lombok.Setter;
import org.agh.backend.model.Duty;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class DutyDto {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime finish;
    private String officeName;

    public DutyDto(Duty duty) {
        this.id = duty.getId();
        this.start = duty.getStart();
        this.finish = duty.getFinish();
        this.officeName = duty.getOffice().getName();
    }
}
