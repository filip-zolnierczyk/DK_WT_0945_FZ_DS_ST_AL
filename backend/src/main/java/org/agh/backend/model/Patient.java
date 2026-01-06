package org.agh.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;

    @Column(unique = true, nullable = false)
    private String pesel;

    private String address;

    public Patient(String name, String surname, String pesel, String address) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.address = address;
    }
}
