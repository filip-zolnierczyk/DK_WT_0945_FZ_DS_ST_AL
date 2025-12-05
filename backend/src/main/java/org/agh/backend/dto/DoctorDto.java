package org.agh.backend.dto;

import org.agh.backend.model.Doctor;

public class DoctorDto {
    public String name;
    public String surname;
    public String specialization;

    public DoctorDto(Doctor doctor) {
        this.name = doctor.getName();
        this.surname = doctor.getSurname();
        this.specialization = doctor.getSpecialization().getName();
    }

}
