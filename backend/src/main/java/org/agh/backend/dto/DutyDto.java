package org.agh.backend.dto;

import org.agh.backend.model.Duty;

import java.time.LocalDateTime;

import lombok.Getter;
import org.agh.backend.model.Specialization;

@Getter
public class DutyDto {

    private final Long id;
    private final LocalDateTime start;
    private final LocalDateTime finish;
    private final String officeName;
    private final String doctorName;
    private final String doctorSurname;
    private final Specialization specialization;

    public DutyDto(Duty duty) {
        this.id = duty.getId();
        this.start = duty.getStart();
        this.finish = duty.getFinish();
        this.officeName = duty.getOffice().getName();
        this.doctorName = duty.getDoctor().getName();
        this.doctorSurname = duty.getDoctor().getSurname();
        this.specialization = duty.getDoctor().getSpecialization();
    }
}
