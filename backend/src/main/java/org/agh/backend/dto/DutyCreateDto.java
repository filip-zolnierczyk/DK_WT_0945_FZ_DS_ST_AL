package org.agh.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class DutyCreateDto {

    private Long doctorId;
    private Long officeId;
    private LocalDateTime start;
    private LocalDateTime finish;
}
