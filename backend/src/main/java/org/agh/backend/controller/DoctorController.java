package org.agh.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.agh.backend.dto.DoctorCreateDto;
import org.agh.backend.dto.DoctorDetailedDto;
import org.agh.backend.dto.DoctorDto;
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
    @Operation(
            summary = "Get all doctors",
            description = "Retrieve a list of all doctors in the system."
    )
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of doctors",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class)
                    )
            )
    )
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        List<DoctorDto> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get doctor by ID",
            description = "Retrieve detailed information about a specific doctor using their unique ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved doctor details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDetailedDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Doctor not found"
            )
    })
    public ResponseEntity<DoctorDetailedDto> getDoctorById(@PathVariable Long id) {
        DoctorDetailedDto doctorDetailedDto = doctorService.getDoctorById(id);
        if (doctorDetailedDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(doctorDetailedDto);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete doctor by ID",
            description = "Delete a specific doctor from the system using their unique ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Successfully deleted doctor"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Doctor not found"
            )
    })
    public ResponseEntity<Void> deleteDoctorById(@PathVariable Long id) {
        boolean deleted = doctorService.deleteDoctorById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(
            summary = "Add a new doctor",
            description = "Create a new doctor in the system with the provided details."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created a new doctor"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Doctor with the same PESEL already exists"
            )
    })
    public ResponseEntity<Void> addDoctor(
            @RequestBody DoctorCreateDto doctorCreateDto
    ) {
        boolean added = false;
        try {
            added = doctorService.addDoctor(
                    doctorCreateDto.getName(),
                    doctorCreateDto.getSurname(),
                    doctorCreateDto.getPesel(),
                    doctorCreateDto.getSpecializationName(),
                    doctorCreateDto.getAddress()
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
