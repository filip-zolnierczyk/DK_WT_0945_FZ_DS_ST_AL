package org.agh.backend;

import org.agh.backend.model.Specialization;
import org.agh.backend.repository.DoctorRepository;
import org.agh.backend.repository.SpecializationRepository;
import org.agh.backend.service.DoctorService;
import org.agh.backend.service.SpecializationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SpecializationTests {
    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService doctorService;

    @Mock
    private SpecializationRepository specializationRepository;

    @InjectMocks
    private SpecializationService specializationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void specializationsAreShared() {

        when(specializationRepository
                .save(new Specialization("Cardiology")))
                .thenReturn(new Specialization("Cardiology"));
        when(specializationRepository
                .save(new Specialization("Neurology")))
                .thenReturn(new Specialization("Neurology"));
        when(specializationRepository.findByName("Cardiology"))
                .thenReturn(null) // first doctor
                .thenReturn(new Specialization("Cardiology")); // second doctor
        when(specializationRepository.findByName("Neurology"))
                .thenReturn(null); // third doctor

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

        when(specializationRepository
                .findAll())
                .thenReturn(List.of(
                        new Specialization("Cardiology"),
                        new Specialization("Neurology")
                )
        );

        assertEquals(2,specializationService.getAllSpecializations().size());
    }
}
