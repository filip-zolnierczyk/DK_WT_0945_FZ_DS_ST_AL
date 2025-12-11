package org.agh.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
@Entity
public class Doctor {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String surname;
    private String pesel;
    @ManyToOne
    private Specialization specialization;
    private String address;

    public Doctor(String name, String surname, String pesel, Specialization specialization, String address) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.specialization = specialization;
        this.address = address;
    }

}
