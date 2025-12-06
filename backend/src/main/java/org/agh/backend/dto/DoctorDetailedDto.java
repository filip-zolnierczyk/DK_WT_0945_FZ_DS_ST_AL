package org.agh.backend.dto;

import org.agh.backend.model.Doctor;

public class DoctorDetailedDto {
    private final Long id;
    private final String name;
    private final String surname;
    private final String specialization;
    private final String address;

    public DoctorDetailedDto(Doctor doctor) {
        this.id = doctor.getId();
        this.name = doctor.getName();
        this.surname = doctor.getSurname();
        this.specialization = doctor.getSpecialization().getName();
        this.address = doctor.getAddress();
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getSpecialization() {
        return specialization;
    }
    public String getAddress() {
        return address;
    }
}
