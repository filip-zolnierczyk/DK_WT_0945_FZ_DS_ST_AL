package org.agh.backend.service;

import org.agh.backend.dto.DutyDto;
import org.agh.backend.dto.OfficeDto;
import org.agh.backend.model.Office;
import org.agh.backend.repository.OfficeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfficeService {

    private final OfficeRepository officeRepository;

    public OfficeService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    /**
     * Adds office
     * @param name name of the office
     * @param address address of the office
     * @param description description of the office
     * @return OfficeDto representing the office
     */
    public OfficeDto addOffice(String name, String address, String description) {
        return new OfficeDto(officeRepository.save(new Office(name, address, description)));
    }

    /**
     * Retrieves information about all offices
     * @return list of all OfficeDto
     */
    public List<OfficeDto> getAllOffices() {
        return officeRepository.findAll()
                .stream()
                .map(OfficeDto::new)
                .toList();
    }

    /**
     * Deletes office
     * Checks if no duties belong to that office
     * @param id the ID of the office
     * @return true if successfully deleted, false if it does not exist
     * @throws IllegalStateException if any duties belong to the office
     */
    public boolean deleteOfficeByIdWithCheck(Long id) {
        Office office = officeRepository.findById(id).orElse(null);

        if (office == null) {
            return false; // gabinet nie istnieje
        }

        // sprawdzamy, czy gabinet ma dyżury
        if (office.getDuties() != null && !office.getDuties().isEmpty()) {
            throw new IllegalStateException("Cannot delete office with assigned duties");
        }

        officeRepository.delete(office);
        return true;
    }

    /**
     * Retrieves OfficeDto by ID
     * @param id the ID of the office
     * @return OfficeDto representing the office
     */
    public OfficeDto getOfficeDtoById(Long id) {
        Office office = officeRepository.findById(id).orElse(null);
        if (office == null) {
            return null;
        }
        return new OfficeDto(office);
    }

    private Office getOfficeById(Long id) {
        return officeRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves all duties of an office
     * @param id the ID of the office
     * @return list of duties of an office if it exists
     * @throws IllegalStateException if office does not exist
     */
    public List<DutyDto> getDutiesOfOffice(Long id) {
        Office office = getOfficeById(id);

        if (office == null) {
            throw new IllegalStateException("Office does not exist");
        }

        return office.getDuties().stream()
                .map(DutyDto::new)
                .toList();
    }
}