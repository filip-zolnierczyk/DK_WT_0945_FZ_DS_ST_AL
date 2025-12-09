package org.agh.backend;

import jakarta.transaction.Transactional;
import org.agh.backend.dto.DoctorCreateDto;
import org.agh.backend.dto.DoctorDetailedDto;
import org.agh.backend.dto.DoctorDto;
import org.agh.backend.model.Doctor;
import org.agh.backend.service.DoctorService;
import org.agh.backend.service.SpecializationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DoctorTests {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private SpecializationService specializationService;

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
                doctorCreateDto.getSpecialization(),
                doctorCreateDto.getAddress()
        ));
        assertEquals(1, doctorService.getAllDoctors().size());
    }

    @Test
    void addingDoctorWithExistingPesel() {
        DoctorCreateDto doctorCreateDto1 = new DoctorCreateDto(
                "John",
                "Doe",
                "123456",
                "Cardiology",
                "Street 123"
        );
        DoctorCreateDto doctorCreateDto2 = new DoctorCreateDto(
                "Anna",
                "Carmichael",
                "123456",
                "Psychiatry",
                "Street 321"
        );

        doctorService.addDoctor(
                doctorCreateDto1.getName(),
                doctorCreateDto1.getSurname(),
                doctorCreateDto1.getPesel(),
                doctorCreateDto1.getSpecialization(),
                doctorCreateDto1.getAddress()
        );
        assertFalse(doctorService.addDoctor(
                doctorCreateDto2.getName(),
                doctorCreateDto2.getSurname(),
                doctorCreateDto2.getPesel(),
                doctorCreateDto2.getSpecialization(),
                doctorCreateDto2.getAddress()
        ));
        assertEquals(1, doctorService.getAllDoctors().size());
    }

    @Test
    void deletingExistingDoctorWorks() {
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
                doctorCreateDto.getSpecialization(),
                doctorCreateDto.getAddress()
        );
        Long doctorId = doctorService.getDoctorByPesel("123456").getId();

        // Deleting existing doctor should return true
        assertTrue(doctorService.deleteDoctorById(doctorId));
        assertEquals(0, doctorService.getAllDoctors().size());
    }

    @Test
    void deletingUnexistingDoctorReturnsFalse() {
        assertFalse(doctorService.deleteDoctorById(1L));
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
        assertThrows(IllegalArgumentException.class, () -> doctorService.addDoctor(
                "John",
                null,
                "123456",
                "Cardiology",
                "Street 123"
        ));
        assertThrows(IllegalArgumentException.class, () -> doctorService.addDoctor(
                "John",
                "Doe",
                null,
                "Cardiology",
                "Street 123"
        ));
        assertThrows(IllegalArgumentException.class, () -> doctorService.addDoctor(
                "John",
                "Doe",
                "123456",
                null,
                "Street 123"
        ));
        assertThrows(IllegalArgumentException.class, () -> doctorService.addDoctor(
                "John",
                "Doe",
                "123456",
                "Cardiology",
                null
        ));
        assertEquals(0, doctorService.getAllDoctors().size());
    }

    @Test
    void testDoctorGetters() {
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
                doctorCreateDto.getSpecialization(),
                doctorCreateDto.getAddress()
        );
        DoctorDetailedDto doctorByPesel = doctorService.getDoctorByPesel("123456");
        DoctorDetailedDto doctorById = doctorService.getDoctorById(doctorByPesel.getId());

        assertEquals(doctorByPesel.getName(), doctorCreateDto.getName());
        assertEquals(doctorById.getName(), doctorCreateDto.getName());
        assertEquals(doctorByPesel.getSurname(), doctorCreateDto.getSurname());
        assertEquals(doctorById.getSurname(), doctorCreateDto.getSurname());
        assertEquals(doctorByPesel.getSpecialization(), doctorCreateDto.getSpecialization());
        assertEquals(doctorById.getSpecialization(), doctorCreateDto.getSpecialization());
        assertEquals(doctorByPesel.getAddress(), doctorCreateDto.getAddress());
        assertEquals(doctorById.getAddress(), doctorCreateDto.getAddress());
    }

    @Test
    void getAllDoctorsList() {
        DoctorCreateDto doctorCreateDto1 = new DoctorCreateDto(
                "John",
                "Doe",
                "123456",
                "Cardiology",
                "Street 123"
        );
        DoctorCreateDto doctorCreateDto2 = new DoctorCreateDto(
                "Anna",
                "Carmichael",
                "654321",
                "Psychiatry",
                "Street 321"
        );

        doctorService.addDoctor(
                doctorCreateDto1.getName(),
                doctorCreateDto1.getSurname(),
                doctorCreateDto1.getPesel(),
                doctorCreateDto1.getSpecialization(),
                doctorCreateDto1.getAddress()
        );
        doctorService.addDoctor(
                doctorCreateDto2.getName(),
                doctorCreateDto2.getSurname(),
                doctorCreateDto2.getPesel(),
                doctorCreateDto2.getSpecialization(),
                doctorCreateDto2.getAddress()
        );

        List<DoctorDto> allDoctors = doctorService.getAllDoctors();
        assertEquals(2, allDoctors.size());
        DoctorDto doctorDto1 = allDoctors.getFirst();
        DoctorDto doctorDto2 = allDoctors.getLast();
        assertEquals(doctorCreateDto1.getName(), doctorDto1.getName());
        assertEquals(doctorCreateDto2.getName(), doctorDto2.getName());
        assertEquals(doctorCreateDto1.getSurname(), doctorDto1.getSurname());
        assertEquals(doctorCreateDto2.getSurname(), doctorDto2.getSurname());
        assertEquals(doctorCreateDto1.getSpecialization(), doctorDto1.getSpecialization());
        assertEquals(doctorCreateDto2.getSpecialization(), doctorDto2.getSpecialization());
    }

}
