package org.agh.backend.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DutyCreateDto {

    private Long doctorId;
    private Long officeId;
    private LocalDateTime start;
    private LocalDateTime end;
}
