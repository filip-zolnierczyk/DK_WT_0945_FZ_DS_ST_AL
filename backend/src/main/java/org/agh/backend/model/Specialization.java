package org.agh.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Specialization {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Specialization() {}

    public Specialization(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
