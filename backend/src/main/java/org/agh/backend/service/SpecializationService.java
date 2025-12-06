package org.agh.backend.service;

import org.agh.backend.model.Specialization;
import org.agh.backend.repository.SpecializationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecializationService {
    private final SpecializationRepository specializationRepository;

    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    public boolean addSpecialization(String name) {
        if (specializationRepository.existsByName(name)) {
            return false;
        }
        specializationRepository.save(new Specialization(name));
        return true;
    }

    public List<Specialization> getAllSpecializations() {
        return specializationRepository.findAll();
    }

}
