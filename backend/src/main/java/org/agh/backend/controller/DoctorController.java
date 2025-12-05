package org.agh.backend.controller;

import org.agh.backend.dto.DoctorCreateDto;
import org.agh.backend.dto.DoctorDetailedDto;
import org.agh.backend.dto.DoctorDto;
import org.agh.backend.model.Doctor;
import org.agh.backend.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/doctors")
@RestController
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        List<DoctorDto> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDetailedDto> getDoctorById(@PathVariable Long id) {
        DoctorDetailedDto doctorDetailedDto = doctorService.getDoctorById(id);
        if (doctorDetailedDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(doctorDetailedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctorById(@PathVariable Long id) {
        boolean deleted = doctorService.deleteDoctorById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> addDoctor(
            @RequestBody DoctorCreateDto doctorCreateDto
    ) {
        boolean added = false;
        try {
            added = doctorService.addDoctor(
                    doctorCreateDto.name,
                    doctorCreateDto.surname,
                    doctorCreateDto.pesel,
                    doctorCreateDto.specializationName,
                    doctorCreateDto.address
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        if (!added) {
            return ResponseEntity.status(409).build();
        }
        return ResponseEntity.status(201).build();
    }


}
