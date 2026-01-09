package org.agh.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

}
