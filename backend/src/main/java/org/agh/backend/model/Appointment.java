package org.agh.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter @NoArgsConstructor
@Entity
public class Appointment {
    public final static int LENGTH = 15; // Time in minutes

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Duty duty;

    @ManyToOne(fetch = FetchType.LAZY)
    private Patient patient;

    // Duty consists of dutyTime/LENGTH slots
    private LocalDateTime startTime;

    public Appointment(Duty duty, Patient patient, LocalDateTime startTime) {
        this.duty = duty;
        this.patient = patient;
        this.startTime = startTime;
    }

    public LocalDateTime getFinishTime() {
        return startTime.plusMinutes(LENGTH);
    }
}
