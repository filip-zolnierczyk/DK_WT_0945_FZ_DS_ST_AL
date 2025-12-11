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

    /**
     * Adds a new specialization if it does not already exist.
     * @param name the name of the specialization to add
     * @return true if the specialization was added, false if it already exists
     */
    public boolean addSpecialization(String name) {
        if (specializationRepository.existsByName(name)) {
            return false;
        }
        specializationRepository.save(new Specialization(name));
        return true;
    }

    /**
     * Retrieves all specializations.
     * @return a list of all specializations
     */
    public List<Specialization> getAllSpecializations() {
        return specializationRepository.findAll();
    }

}
