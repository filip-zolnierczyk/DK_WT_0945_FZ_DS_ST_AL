package org.agh.backend.dto;

import lombok.Getter;

@Getter
public class DoctorCreateDto {
    private final String name;
    private final String surname;
    private final String pesel;
    private final String specializationName;
    private final String address;

    public DoctorCreateDto(String name, String surname, String pesel, String specialization, String address) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.specializationName = specialization;
        this.address = address;
    }

}
