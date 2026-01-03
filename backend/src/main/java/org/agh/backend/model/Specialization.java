package org.agh.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
@Entity
public class Specialization {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Specialization(String name) {
        this.name = name;
    }

}
