package org.agh.backend;

import org.agh.backend.model.Doctor;
import org.agh.backend.model.Duty;
import org.agh.backend.model.Office;
import org.agh.backend.repository.DutyRepository;
import org.agh.backend.service.DutyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DutyServiceTests {

    @Mock
    private DutyRepository dutyRepository;

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

        Duty duty = dutyService.createDuty(doctor, office, start, end);

        assertNotNull(duty);
        assertEquals(doctor, duty.getDoctor());
        assertEquals(office, duty.getOffice());
        assertEquals(start, duty.getStart());
        assertEquals(end, duty.getEnd());
    }

    @Test
    void createDutyFailsWhenStartOrEndNull() {
        assertThrows(IllegalArgumentException.class,
                () -> dutyService.createDuty(doctor, office, null, end));
        assertThrows(IllegalArgumentException.class,
                () -> dutyService.createDuty(doctor, office, start, null));
    }

    @Test
    void createDutyFailsWhenEndBeforeStart() {
        LocalDateTime badEnd = start.minusHours(1);
        assertThrows(IllegalArgumentException.class,
                () -> dutyService.createDuty(doctor, office, start, badEnd));
    }

    @Test
    void createDutyFailsWhenDoctorBusy() {
        when(dutyRepository.existsByDoctorAndStartLessThanAndFinishGreaterThan(doctor, end, start))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> dutyService.createDuty(doctor, office, start, end));
    }

    @Test
    void createDutyFailsWhenOfficeBusy() {
        when(dutyRepository.existsByDoctorAndStartLessThanAndFinishGreaterThan(doctor, end, start))
                .thenReturn(false);
        when(dutyRepository.existsByOfficeAndStartLessThanAndFinishGreaterThan(office, end, start))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> dutyService.createDuty(doctor, office, start, end));
    }

    @Test
    void deleteDutySuccessfully() {
        when(dutyRepository.existsById(1L)).thenReturn(true);
        doNothing().when(dutyRepository).deleteById(1L);

        assertDoesNotThrow(() -> dutyService.deleteDuty(1L));
        verify(dutyRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteDutyFailsWhenNotExist() {
        when(dutyRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> dutyService.deleteDuty(1L));
    }
}
