package org.agh.backend;

import org.agh.backend.dto.AppointmentListDto;
import org.agh.backend.model.*;
import org.agh.backend.repository.AppointmentRepository;
import org.agh.backend.repository.DutyRepository;
import org.agh.backend.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AppointmentServiceTests {
    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DutyRepository dutyRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Duty duty;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Specialization specialization = mock(Specialization.class);
        when(specialization.getName()).thenReturn("Kardiologia");

        Doctor doctor = mock(Doctor.class);
        when(doctor.getSpecialization()).thenReturn(specialization);
        when(doctor.getName()).thenReturn("Kowalski");

        Office office = mock(Office.class);
        when(office.getName()).thenReturn("Gabinet Kardiologii");

        duty = mock(Duty.class);
        when(duty.getId()).thenReturn(1L);
        when(duty.getStart()).thenReturn(LocalDateTime.now().plusDays(1));
        when(duty.getFinish()).thenReturn(LocalDateTime.now().plusDays(1).plusHours(1));
        when(duty.getDoctor()).thenReturn(doctor);
        when(duty.getOffice()).thenReturn(office);
    }

    @Test
    void gettingAppointmentsListOfAnUnexistingDuty() {
        when(dutyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> appointmentService.getAppointmentListByDutyId(1L)
        );
    }

    @Test
    void gettingAppointmentsListWithOccupiedAppointment() {
        when(dutyRepository.findById(1L)).thenReturn(Optional.of(duty));

        Patient patient = mock(Patient.class);
        Appointment appointment = new Appointment(duty, patient, duty.getStart());

        when(appointmentRepository.findAllByDuty(duty))
                .thenReturn(List.of(appointment));

        List<AppointmentListDto> result = appointmentService.getAppointmentListByDutyId(1L);

        AppointmentListDto slot = result.getFirst();
        assertEquals(duty.getStart(), slot.getStartTime());
        assertEquals(duty.getStart().plusMinutes(Appointment.LENGTH), slot.getFinishTime());
        assertTrue(slot.isOccupied());
        assertEquals("Kowalski", slot.getDoctorName());
        assertEquals("Kardiologia", slot.getSpecializationName());
        assertEquals("Gabinet Kardiologii", slot.getOfficeName());
    }

    @Test
    void gettingAppointmentsListWithoutOccupiedAppointment() {
        when(dutyRepository.findById(1L)).thenReturn(Optional.of(duty));

        when(appointmentRepository.findAllByDuty(duty))
                .thenReturn(List.of());

        List<AppointmentListDto> result = appointmentService.getAppointmentListByDutyId(1L);

        AppointmentListDto slot = result.getFirst();
        assertEquals(duty.getStart(), slot.getStartTime());
        assertEquals(duty.getStart().plusMinutes(Appointment.LENGTH), slot.getFinishTime());
        assertFalse(slot.isOccupied());
        assertEquals("Kowalski", slot.getDoctorName());
        assertEquals("Kardiologia", slot.getSpecializationName());
        assertEquals("Gabinet Kardiologii", slot.getOfficeName());
    }

    @Test
    void cancelingAnAppointment() {
        Long appointmentId = 1L;

        when(appointmentRepository.existsById(appointmentId)).thenReturn(true)
                .thenThrow(IllegalArgumentException.class);

        assertDoesNotThrow(() -> appointmentService.cancelAppointment(appointmentId));
        assertThrows(IllegalArgumentException.class, () -> appointmentService.cancelAppointment(appointmentId));
    }

}
