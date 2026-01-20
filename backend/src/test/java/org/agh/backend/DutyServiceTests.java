package org.agh.backend;

import org.agh.backend.dto.DutyCreateDto;
import org.agh.backend.dto.DutyDto;
import org.agh.backend.model.Doctor;
import org.agh.backend.model.Duty;
import org.agh.backend.model.Office;
import org.agh.backend.repository.DoctorRepository;
import org.agh.backend.repository.DutyRepository;
import org.agh.backend.repository.OfficeRepository;
import org.agh.backend.service.DutyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DutyServiceTests {

    @Mock
    private DutyRepository dutyRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private OfficeRepository officeRepository;

    @InjectMocks
    private DutyService dutyService;

    private Doctor doctor;
    private Office office;
    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        doctor = new Doctor("John", "Doe", "123456", null, "Street 123");
        office = new Office("Office 1", "Main St", null);

        start = LocalDateTime.of(2026, 1, 10, 9, 0);
        end = LocalDateTime.of(2026, 1, 10, 17, 0);
    }

    @Test
    void createDutySuccessfully() {
        when(dutyRepository.existsByDoctorAndStartLessThanAndFinishGreaterThan(doctor, end, start))
                .thenReturn(false);
        when(dutyRepository.existsByOfficeAndStartLessThanAndFinishGreaterThan(office, end, start))
                .thenReturn(false);
        when(dutyRepository.save(any(Duty.class))).thenAnswer(i -> i.getArguments()[0]);
        when(doctorRepository.findById(any()))
                .thenReturn(Optional.of(doctor));
        when(officeRepository.findById(any()))
                .thenReturn(Optional.of(office));

        DutyCreateDto dutyCreateDto = new DutyCreateDto(doctor.getId(), office.getId(), start, end);

        DutyDto dutyDto = dutyService.addDuty(dutyCreateDto);

        assertNotNull(dutyDto);
        assertEquals(doctor.getName(), dutyDto.getDoctorName());
        assertEquals(office.getName(), dutyDto.getOfficeName());
        assertEquals(start, dutyDto.getStart());
        assertEquals(end, dutyDto.getFinish());
    }

    @Test
    void createDutyFailsWhenStartOrEndNull() {
        DutyCreateDto dutyCreateDto1 = new DutyCreateDto(doctor.getId(), office.getId(), null, end);
        DutyCreateDto dutyCreateDto2 = new DutyCreateDto(doctor.getId(), office.getId(), start, null);

        assertThrows(IllegalArgumentException.class,
                () -> dutyService.addDuty(dutyCreateDto1));
        assertThrows(IllegalArgumentException.class,
                () -> dutyService.addDuty(dutyCreateDto2));
    }

    @Test
    void createDutyFailsWhenEndBeforeStart() {
        LocalDateTime badEnd = start.minusHours(1);

        DutyCreateDto dutyCreateDto = new DutyCreateDto(doctor.getId(), office.getId(), start, badEnd);

        assertThrows(IllegalArgumentException.class,
                () -> dutyService.addDuty(dutyCreateDto));
    }

    @Test
    void createDutyFailsWhenDoctorBusy() {
        when(dutyRepository.existsByDoctorAndStartLessThanAndFinishGreaterThan(doctor, end, start))
                .thenReturn(true);
        when(doctorRepository.findById(any()))
                .thenReturn(Optional.of(doctor));
        when(officeRepository.findById(any()))
                .thenReturn(Optional.of(office));

        DutyCreateDto dutyCreateDto = new DutyCreateDto(doctor.getId(), office.getId(), start, end);

        assertThrows(IllegalStateException.class,
                () -> dutyService.addDuty(dutyCreateDto));
    }

    @Test
    void createDutyFailsWhenOfficeBusy() {
        when(dutyRepository.existsByDoctorAndStartLessThanAndFinishGreaterThan(doctor, end, start))
                .thenReturn(false);
        when(dutyRepository.existsByOfficeAndStartLessThanAndFinishGreaterThan(office, end, start))
                .thenReturn(true);
        when(doctorRepository.findById(any()))
                .thenReturn(Optional.of(doctor));
        when(officeRepository.findById(any()))
                .thenReturn(Optional.of(office));

        DutyCreateDto dutyCreateDto = new DutyCreateDto(doctor.getId(), office.getId(), start, end);

        assertThrows(IllegalStateException.class,
                () -> dutyService.addDuty(dutyCreateDto));
    }

    @Test
    void deleteDutySuccessfully() {
        Duty duty = new Duty();

        when(dutyRepository.findById(1L)).thenReturn(Optional.of(duty));
        doNothing().when(dutyRepository).deleteById(1L);

        assertDoesNotThrow(() -> dutyService.deleteDuty(1L));
        verify(dutyRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteDutyFailsWhenNotExist() {
        when(dutyRepository.existsById(1L)).thenReturn(false);

        assertFalse(dutyService.deleteDuty(1L));
    }
}
