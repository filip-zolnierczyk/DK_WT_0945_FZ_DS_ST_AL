package org.agh.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.agh.backend.dto.AppointmentListDto;
import org.agh.backend.dto.PatientCreateDto;
import org.agh.backend.dto.PatientDto;
import org.agh.backend.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/patients")
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "Get all patients")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of patients")
    })
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        List<PatientDto> patientDtos = patientService.getAllPatients();
        return ResponseEntity.ok(patientDtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved patient"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(patientService.getPatientById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Add a new patient")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created a new patient"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Patient with the same PESEL already exists")
    })
    public ResponseEntity<Void> addPatient(@RequestBody PatientCreateDto dto) {
        boolean added;
        try {
            added = patientService.addPatient(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        if (!added) {
            return ResponseEntity.status(409).build();
        }
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully deleted patient"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        try {
            boolean deleted = patientService.deletePatient(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();
        }
    }

    @GetMapping("/{id}/appointments")
    @Operation(
            summary = "Retrieve all of patient's appointments."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved patient's appointments."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Patient not found."
            )
    })
    public ResponseEntity<List<AppointmentListDto>> getAppointmentListByPatientId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(patientService.getAppointmentListByPatientId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
