package org.agh.backend;

import jakarta.annotation.PostConstruct;
import org.agh.backend.service.DoctorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InitExampleDatabase {
    private final DoctorService doctorService;
    private final boolean init;

    public InitExampleDatabase(DoctorService doctorService,
                               @Value("${example.database:false}") boolean init
    ) {
        this.doctorService = doctorService;
        this.init = init;
    }

    @PostConstruct
    public void init() {
        if (!init || !doctorService.getAllDoctors().isEmpty()) {
            return;
        }

        doctorService.addDoctor("Jan", "Kowalski", "89012345678", "Kardiologia", "ul. Słoneczna 12, Warszawa");
        doctorService.addDoctor("Anna", "Nowak", "75098765432", "Kardiologia", "ul. Kwiatowa 5, Kraków");
        doctorService.addDoctor("Piotr", "Wiśniewski", "62045678901", "Kardiologia", "ul. Leśna 9, Gdańsk");
        doctorService.addDoctor("Katarzyna", "Wójcik", "88023456789", "Pediatria", "ul. Długa 23, Wrocław");
        doctorService.addDoctor("Tomasz", "Kowalczyk", "99034567890", "Pediatria", "ul. Krótka 7, Poznań");
        doctorService.addDoctor("Magdalena", "Kamińska", "67012349876", "Dermatologia", "ul. Lipowa 18, Łódź");
        doctorService.addDoctor("Marcin", "Zieliński", "81056723490", "Neurologia", "ul. Parkowa 11, Lublin");

        System.out.println("Example database initialized.");
    }
}
