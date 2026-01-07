package org.agh.backend.service;

import org.agh.backend.model.Office;
import org.agh.backend.repository.DutyRepository;
import org.agh.backend.repository.OfficeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfficeService {

    private final OfficeRepository officeRepository;

    public OfficeService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    public Office addOffice(String name, String address, String description) {
        return officeRepository.save(new Office(name, address, description));
    }

    public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }

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

}

