package org.agh.backend.dto;

import lombok.Getter;

@Getter
public class PatientCreateDto {
    private String name;
    private String surname;
    private String pesel;
    private String address;
}
