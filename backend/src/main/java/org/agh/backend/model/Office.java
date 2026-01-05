package org.agh.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter @NoArgsConstructor
@Entity
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String description;

    @OneToMany(mappedBy = "office")
    private List<Duty> duties;

    public Office(String name, String address, String description) {
        this.name = name;
        this.address = address;
        this.description = description;
    }
}
