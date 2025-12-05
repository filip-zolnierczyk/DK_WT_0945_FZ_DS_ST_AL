package org.agh.backend;

import jakarta.annotation.PostConstruct;
import org.agh.backend.model.Doctor;
import org.agh.backend.service.DoctorService;
import org.springframework.stereotype.Component;

// TODO: Make this optional
@Component
public class InitExampleDatabase {

    private final DoctorService doctorService;

    public InitExampleDatabase(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // TODO: Make realistic data
    @PostConstruct
    public void init() {
        Doctor doctor1 = new Doctor(
                "Name1",
                "Surname1",
                "Pesel1",
                "Specialization1",
                "Address1"
        );

        Doctor doctor2 = new Doctor(
                "Name2",
                "Surname2",
                "Pesel2",
                "Specialization2",
                "Address2"
        );

        Doctor doctor3 = new Doctor(
                "Name3",
                "Surname3",
                "Pesel3",
                "Specialization3",
                "Address3"
        );

        Doctor doctor4 = new Doctor(
                "Name4",
                "Surname4",
                "Pesel4",
                "Specialization4",
                "Address4"
        );

        Doctor doctor5 = new Doctor(
                "Name5",
                "Surname5",
                "Pesel5",
                "Specialization5",
                "Address5"
        );

        Doctor doctor6 = new Doctor(
                "Name6",
                "Surname6",
                "Pesel6",
                "Specialization6",
                "Address6"
        );

        Doctor doctor7 = new Doctor(
                "Name7",
                "Surname7",
                "Pesel7",
                "Specialization7",
                "Address7"
        );

        doctorService.addDoctor(doctor1);
        doctorService.addDoctor(doctor2);
        doctorService.addDoctor(doctor3);
        doctorService.addDoctor(doctor4);
        doctorService.addDoctor(doctor5);
        doctorService.addDoctor(doctor6);
        doctorService.addDoctor(doctor7);

        System.out.println("Example database initialized.");
    }
}
