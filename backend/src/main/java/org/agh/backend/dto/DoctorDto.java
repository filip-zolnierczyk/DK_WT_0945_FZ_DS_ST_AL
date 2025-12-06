package org.agh.backend.dto;

import org.agh.backend.model.Doctor;

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

}
