package org.agh.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class DutyCreateDto {

    private final Long doctorId;
    private final Long officeId;
    private final LocalDateTime start;
    private final LocalDateTime finish;

    public DutyCreateDto(Long doctorId,
                         Long officeId,
                         LocalDateTime start,
                         LocalDateTime finish
    ) {
        this.doctorId = doctorId;
        this.officeId = officeId;
        this.start = start;
        this.finish = finish;
    }
}
