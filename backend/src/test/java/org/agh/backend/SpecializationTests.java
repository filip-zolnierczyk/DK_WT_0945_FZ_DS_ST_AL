package org.agh.backend;

import jakarta.transaction.Transactional;
import org.agh.backend.service.DoctorService;
import org.agh.backend.service.SpecializationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SpecializationTests {
    @Autowired
    DoctorService doctorService;

    @Autowired
    SpecializationService specializationService;

    @Test
    void specializationsAreShared() {
        doctorService.addDoctor(
                "John",
                "Doe",
                "123456",
                "Cardiology",
                "Street 123"
        );
        doctorService.addDoctor(
                "Jane",
                "Smith",
                "654321",
                "Cardiology",
                "Avenue 456"
        );
        doctorService.addDoctor(
                "Alice",
                "Johnson",
                "112233",
                "Neurology",
                "Boulevard 789"
        );

        assertEquals(2, specializationService.getAllSpecializations().size());

    }
}
