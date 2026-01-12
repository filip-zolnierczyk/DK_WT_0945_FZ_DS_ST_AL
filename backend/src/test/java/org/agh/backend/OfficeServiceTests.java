package org.agh.backend;

import org.agh.backend.dto.OfficeDto;
import org.agh.backend.model.Duty;
import org.agh.backend.model.Office;
import org.agh.backend.repository.OfficeRepository;
import org.agh.backend.service.OfficeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OfficeServiceTests {

    @Mock
    private OfficeRepository officeRepository;

    @InjectMocks
    private OfficeService officeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addingOfficeWorks() {
        Office office = new Office("Main Office", "Street 123", "Main description");

        when(officeRepository.save(any(Office.class))).thenReturn(office);

        OfficeDto saved = officeService.addOffice("Main Office", "Street 123", "Main description");

        assertNotNull(saved);
        assertEquals("Main Office", saved.getName());
        assertEquals("Street 123", saved.getAddress());
        assertEquals("Main description", saved.getDescription());
    }

    @Test
    void getAllOfficesWorks() {
        when(officeRepository.findAll()).thenReturn(List.of(
                new Office("A", "Addr A", null),
                new Office("B", "Addr B", null)
        ));

        List<Office> offices = officeService.getAllOffices();

        assertEquals(2, offices.size());
        assertEquals("A", offices.get(0).getName());
        assertEquals("B", offices.get(1).getName());
    }

    @Test
    void deletingExistingOfficeWithoutDutiesWorks() {
        Office office = new Office("Main", "Addr", "Desc");

        when(officeRepository.findById(1L)).thenReturn(Optional.of(office));

        boolean result = officeService.deleteOfficeByIdWithCheck(1L);

        assertTrue(result);
        verify(officeRepository, times(1)).delete(office);
    }

    @Test
    void deletingNonExistingOfficeReturnsFalse() {
        when(officeRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = officeService.deleteOfficeByIdWithCheck(1L);

        assertFalse(result);
        verify(officeRepository, never()).delete(any());
    }

    @Test
    void deletingOfficeWithDutiesThrowsException() {
        Office office = new Office("Main", "Addr", "Desc");
        // dodajemy dyżur do faktycznej listy w obiekcie Office
        office.getDuties().add(new Duty());

        when(officeRepository.findById(1L)).thenReturn(Optional.of(office));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> officeService.deleteOfficeByIdWithCheck(1L)
        );

        assertEquals("Cannot delete office with assigned duties", ex.getMessage());
        verify(officeRepository, never()).delete(any());
    }

}
