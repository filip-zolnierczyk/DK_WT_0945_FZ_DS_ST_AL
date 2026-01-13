package org.agh.backend.dto;

import lombok.Setter;
import org.agh.backend.model.Duty;

import java.time.LocalDateTime;

import lombok.Getter;
import org.agh.backend.model.Specialization;

@Getter
public class DutyDto {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime finish;
    private String officeName;
    private String doctorName;
    private String doctorSurname;
    private Specialization specialization;

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
