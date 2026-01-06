package org.agh.backend.dto;

import lombok.Getter;
import org.agh.backend.model.Patient;

@Getter
public class PatientDto {

    private final Long id;
    private final String name;
    private final String surname;
    private final String pesel;
    private final String address;

    public PatientDto(Patient patient) {
        this.id = patient.getId();
        this.name = patient.getName();
        this.surname = patient.getSurname();
        this.pesel = patient.getPesel();
        this.address = patient.getAddress();
    }
}
