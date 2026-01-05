package org.agh.backend.service;

import org.agh.backend.repository.DutyRepository;
import org.springframework.stereotype.Service;

@Service
public class DutyService {
    private DutyRepository dutyRepository;

    public DutyService(DutyRepository dutyRepository) {
        this.dutyRepository = dutyRepository;
    }


}
