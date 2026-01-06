package org.agh.backend.service;

import org.agh.backend.repository.DutyRepository;
import org.agh.backend.repository.OfficeRepository;
import org.springframework.stereotype.Service;

@Service
public class OfficeService {
    private OfficeRepository officeRepository;

    public OfficeService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }




}
