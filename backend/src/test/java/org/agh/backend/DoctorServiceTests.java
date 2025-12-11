package org.agh.backend;

import org.agh.backend.dto.DoctorCreateDto;
import org.agh.backend.dto.DoctorDetailedDto;
import org.agh.backend.dto.DoctorDto;
import org.agh.backend.model.Doctor;
import org.agh.backend.model.Specialization;
import org.agh.backend.repository.DoctorRepository;
import org.agh.backend.repository.SpecializationRepository;
import org.agh.backend.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DoctorServiceTests {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private SpecializationRepository specializationRepository;

    @InjectMocks
    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addingDoctorWorks() {
        DoctorCreateDto doctorCreateDto = new DoctorCreateDto(
                "John",
                "Doe",
                "123456",
                "Cardiology",
                "Street 123"
        );
        Doctor doctor = new Doctor(
                doctorCreateDto.getName(),
                doctorCreateDto.getSurname(),
                doctorCreateDto.getPesel(),
                new Specialization(doctorCreateDto.getSpecialization()),
                doctorCreateDto.getAddress()
        );

        when(specializationRepository
                .findByName(doctorCreateDto.getSpecialization()))
                .thenReturn(new Specialization(doctorCreateDto.getSpecialization()));
        when(doctorRepository
                .existsByPesel(doctorCreateDto.getPesel()))
                .thenReturn(false);
        when(doctorRepository
                .save(any(Doctor.class)))
                .thenReturn(doctor);
        when(doctorRepository
                .findAll())
                .thenReturn(List.of(doctor));

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

        // Finding specializations
        when(specializationRepository
                .findByName(doctorCreateDto1.getSpecialization()))
                .thenReturn(new Specialization(doctorCreateDto1.getSpecialization()));
        when(specializationRepository
                .findByName(doctorCreateDto2.getSpecialization()))
                .thenReturn(new Specialization(doctorCreateDto2.getSpecialization()));

        // Checking PESEL existence; Order matters!
        when(doctorRepository
                .existsByPesel(doctorCreateDto1.getPesel()))
                .thenReturn(false) // for first doctor
                .thenReturn(true); // for second doctor

        // Only first doctor will be saved
        when(doctorRepository
                .save(any(Doctor.class)))
                .thenReturn(new Doctor(
                        doctorCreateDto1.getName(),
                        doctorCreateDto1.getSurname(),
                        doctorCreateDto1.getPesel(),
                        new Specialization(doctorCreateDto1.getSpecialization()),
                        doctorCreateDto1.getAddress()
                ));
        when(doctorRepository
                .findAll())
                .thenReturn(List.of(new Doctor(
                        doctorCreateDto1.getName(),
                        doctorCreateDto1.getSurname(),
                        doctorCreateDto1.getPesel(),
                        new Specialization(doctorCreateDto1.getSpecialization()),
                        doctorCreateDto1.getAddress()
                )
        ));

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

        // Finding specialization
        when(specializationRepository
                .findByName(doctorCreateDto.getSpecialization()))
                .thenReturn(new Specialization(doctorCreateDto.getSpecialization()));

        when(doctorRepository
                .existsByPesel("123456"))
                .thenReturn(false) // for adding
                .thenReturn(true); // for deleting

        when(doctorRepository
                .save(any(Doctor.class)))
                .thenReturn(new Doctor());
        when(doctorRepository
                .findByPesel(doctorCreateDto.getPesel()))
                .thenReturn(new Doctor(
                        doctorCreateDto.getName(),
                        doctorCreateDto.getSurname(),
                        doctorCreateDto.getPesel(),
                        new Specialization(doctorCreateDto.getSpecialization()),
                        doctorCreateDto.getAddress()
                )
        );

        doctorService.addDoctor(
                doctorCreateDto.getName(),
                doctorCreateDto.getSurname(),
                doctorCreateDto.getPesel(),
                doctorCreateDto.getSpecialization(),
                doctorCreateDto.getAddress()
        );
        Long doctorId = doctorService.getDoctorByPesel("123456").getId();

        when(doctorRepository
                .existsById(doctorId))
                .thenReturn(true) // for deleting existing
                .thenReturn(false); // for checking unexisting

        // Deleting existing doctor should return true
        assertTrue(doctorService.deleteDoctorById(doctorId));
        assertFalse(doctorService.deleteDoctorById(doctorId));
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
        DoctorCreateDto doctorCreateDto = new DoctorCreateDto("John", "Doe", "123456", "Cardiology", "Street 123");

        Doctor savedDoctor = mock(Doctor.class);
        when(savedDoctor
                .getId())
                .thenReturn(1L);
        when(savedDoctor
                .getName())
                .thenReturn(doctorCreateDto.getName());
        when(savedDoctor
                .getSurname())
                .thenReturn(doctorCreateDto.getSurname());
        when(savedDoctor
                .getPesel())
                .thenReturn(doctorCreateDto.getPesel());
        when(savedDoctor
                .getSpecialization())
                .thenReturn(new Specialization(doctorCreateDto.getSpecialization()));
        when(savedDoctor
                .getAddress())
                .thenReturn(doctorCreateDto.getAddress());

        when(specializationRepository
                .findByName(doctorCreateDto.getSpecialization()))
                .thenReturn(new Specialization(doctorCreateDto.getSpecialization()));
        when(doctorRepository
                .save(any(Doctor.class)))
                .thenReturn(savedDoctor);
        when(doctorRepository
                .findByPesel(doctorCreateDto.getPesel()))
                .thenReturn(savedDoctor);
        when(doctorRepository
                .findById(1L))
                .thenReturn(Optional.of(savedDoctor));

        doctorService.addDoctor(
                doctorCreateDto.getName(),
                doctorCreateDto.getSurname(),
                doctorCreateDto.getPesel(),
                doctorCreateDto.getSpecialization(),
                doctorCreateDto.getAddress());

        DoctorDetailedDto byPesel = doctorService.getDoctorByPesel("123456");
        DoctorDetailedDto byId = doctorService.getDoctorById(byPesel.getId());

        assertEquals(doctorCreateDto.getName(), byPesel.getName());
        assertEquals(doctorCreateDto.getName(), byId.getName());
        assertEquals(doctorCreateDto.getSurname(), byPesel.getSurname());
        assertEquals(doctorCreateDto.getSurname(), byId.getSurname());
        assertEquals(doctorCreateDto.getSpecialization(), byPesel.getSpecialization());
        assertEquals(doctorCreateDto.getSpecialization(), byId.getSpecialization());
        assertEquals(doctorCreateDto.getAddress(), byPesel.getAddress());
        assertEquals(doctorCreateDto.getAddress(), byId.getAddress());
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
        Doctor doctor1 = new Doctor(
                doctorCreateDto1.getName(),
                doctorCreateDto1.getSurname(),
                doctorCreateDto1.getPesel(),
                new Specialization(doctorCreateDto1.getSpecialization()),
                doctorCreateDto1.getAddress()
        );
        Doctor doctor2 = new Doctor(
                doctorCreateDto2.getName(),
                doctorCreateDto2.getSurname(),
                doctorCreateDto2.getPesel(),
                new Specialization(doctorCreateDto2.getSpecialization()),
                doctorCreateDto2.getAddress()
        );

        when(specializationRepository
                .findByName(doctorCreateDto1.getSpecialization()))
                .thenReturn(new Specialization(doctorCreateDto1.getSpecialization()));
        when(specializationRepository
                .findByName(doctorCreateDto2.getSpecialization()))
                .thenReturn(new Specialization(doctorCreateDto2.getSpecialization()));
        when(doctorRepository
                .existsByPesel(doctorCreateDto1.getPesel()))
                .thenReturn(false);
        when(doctorRepository
                .existsByPesel(doctorCreateDto2.getPesel()))
                .thenReturn(false);

        when(doctorRepository
                .save(any(Doctor.class)))
                .thenReturn(doctor1)
                .thenReturn(doctor2);

        when(doctorRepository
                .findAll())
                .thenReturn(List.of(doctor1, doctor2));

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
