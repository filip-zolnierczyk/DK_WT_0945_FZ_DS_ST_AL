package org.agh.backend.dto;

import lombok.Getter;

@Getter
public class DoctorCreateDto {
    private final String name;
    private final String surname;
    private final String pesel;
    private final String specialization;
    private final String address;

    public DoctorCreateDto(String name, String surname, String pesel, String specialization, String address) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.specialization = specialization;
        this.address = address;
    }

}
