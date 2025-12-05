package org.agh.backend;

import jakarta.annotation.PostConstruct;
import org.agh.backend.model.Doctor;
import org.agh.backend.model.Specialization;
import org.agh.backend.service.DoctorService;
import org.agh.backend.service.SpecializationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InitExampleDatabase {
    private final DoctorService doctorService;
    private final boolean init;

    public InitExampleDatabase(DoctorService doctorService,
                               SpecializationService specializationService,
                               @Value("${example.database:false}") boolean init
    ) {
        this.doctorService = doctorService;
        this.init = init;
    }

    // TODO: Make realistic data
    @PostConstruct
    public void init() {
        if (!init) {
            return;
        }

        doctorService.addDoctor("Name1", "Surname1", "Pesel1", "Cardiology", "Address1");
        doctorService.addDoctor("Name2", "Surname2", "Pesel2", "Cardiology", "Address2");
        doctorService.addDoctor("Name3", "Surname3", "Pesel3", "Cardiology", "Address3");
        doctorService.addDoctor("Name4", "Surname4", "Pesel4", "Pediatrics", "Address4");
        doctorService.addDoctor("Name5", "Surname5", "Pesel5", "Pediatrics", "Address5");
        doctorService.addDoctor("Name6", "Surname6", "Pesel6", "Dermatology", "Address6");
        doctorService.addDoctor("Name7", "Surname7", "Pesel7", "Specialization4", "Address7");

        System.out.println("Example database initialized.");
    }
}
