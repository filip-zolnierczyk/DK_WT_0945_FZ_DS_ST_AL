package org.agh.backend.dto;

import org.agh.backend.model.Doctor;

public class DoctorDto {
    public Long id;
    public String name;
    public String surname;
    public String specialization;

    public DoctorDto(Doctor doctor) {
        this.id = doctor.getId();
        this.name = doctor.getName();
        this.surname = doctor.getSurname();
        this.specialization = doctor.getSpecialization().getName();
    }

}
