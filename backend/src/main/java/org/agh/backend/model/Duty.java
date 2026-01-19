package org.agh.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @NoArgsConstructor
@Entity
public class Duty {

    @Id
    @GeneratedValue
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "office_id", nullable = false)
    private Office office;

    @Setter
    private LocalDateTime start;

    @Setter
    private LocalDateTime finish;

    @OneToMany(mappedBy = "duty")
    private List<Appointment> appointments = new ArrayList<>();

}
