package org.agh.backend.dto;

import org.agh.backend.model.Doctor;

public class DoctorDetailedDto {
    public Long id;
    public String name;
    public String surname;
    public String specialization;
    public String address;

    public DoctorDetailedDto(Doctor doctor) {
        this.id = doctor.getId();
        this.name = doctor.getName();
        this.surname = doctor.getSurname();
        this.specialization = doctor.getSpecialization().getName();
        this.address = doctor.getAddress();
    }
}
