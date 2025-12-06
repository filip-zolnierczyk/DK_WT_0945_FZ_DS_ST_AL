package org.agh.backend;

import jakarta.transaction.Transactional;
import org.agh.backend.dto.DoctorCreateDto;
import org.agh.backend.service.DoctorService;
import org.agh.backend.service.SpecializationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DoctorTests {
    @Autowired
    DoctorService doctorService;

    @Autowired
    SpecializationService specializationService;

    @Test
    void addingDoctorWorks() {
        DoctorCreateDto doctorCreateDto = new DoctorCreateDto(
                "John",
                "Doe",
                "123456",
                "Cardiology",
                "Street 123"
        );
        assertTrue(doctorService.addDoctor(
                doctorCreateDto.getName(),
                doctorCreateDto.getSurname(),
                doctorCreateDto.getPesel(),
                doctorCreateDto.getSpecializationName(),
                doctorCreateDto.getAddress()
        ));

        // Should fail because of duplicate PESEL
        assertFalse(doctorService.addDoctor(
                doctorCreateDto.getName(),
                doctorCreateDto.getSurname(),
                doctorCreateDto.getPesel(),
                doctorCreateDto.getSpecializationName(),
                doctorCreateDto.getAddress()
        ));

        assertEquals(1, doctorService.getAllDoctors().size());

    }

    @Test
    void deletingDoctorWorks() {
        // Deleting unexisting doctor should return false
        assertFalse(doctorService.deleteDoctorById(1L));

        DoctorCreateDto doctorCreateDto = new DoctorCreateDto(
                "John",
                "Doe",
                "123456",
                "Cardiology",
                "Street 123"
        );
        doctorService.addDoctor(
                doctorCreateDto.getName(),
                doctorCreateDto.getSurname(),
                doctorCreateDto.getPesel(),
                doctorCreateDto.getSpecializationName(),
                doctorCreateDto.getAddress()
        );
        Long doctorId = doctorService.getDoctorByPesel("123456").getId();

        // Deleting existing doctor should return true
        assertTrue(doctorService.deleteDoctorById(doctorId));
        assertEquals(0, doctorService.getAllDoctors().size());
    }

    @Test
    void addingDoctorWithNullValuesThrows() {
        assertThrows(IllegalArgumentException.class, () -> doctorService.addDoctor(
                null,
                "Doe",
                "123456",
                "Cardiology",
                "Street 123"
        ));
        assertEquals(0, doctorService.getAllDoctors().size());
    }

}
