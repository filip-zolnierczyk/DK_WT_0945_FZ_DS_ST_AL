package org.agh.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Doctor {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String surname;
    private String pesel;
    private String specialization;
    private String address;

    public Doctor() {}

    // TODO: Specialization as different model
    public Doctor(String name, String surname, String pesel, String specialization, String address) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.specialization = specialization;
        this.address = address;
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

    public String getPesel() {
        return pesel;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getAddress() {
        return address;
    }
}
