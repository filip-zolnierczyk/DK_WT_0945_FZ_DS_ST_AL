package org.agh.backend.dto;

import lombok.Getter;
import org.agh.backend.model.Doctor;

@Getter
public class DoctorDto {
    private final Long id;
    private final String name;
    private final String surname;
    private final String specialization;

    public DoctorDto(Doctor doctor) {
        this.id = doctor.getId();
        this.name = doctor.getName();
        this.surname = doctor.getSurname();
        this.specialization = doctor.getSpecialization().getName();
    }

}
