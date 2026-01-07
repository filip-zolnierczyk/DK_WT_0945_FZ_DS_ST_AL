package org.agh.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.agh.backend.dto.DutyCreateDto;
import org.agh.backend.dto.DutyDto;
import org.agh.backend.model.Doctor;
import org.agh.backend.model.Duty;
import org.agh.backend.model.Office;
import org.agh.backend.repository.DoctorRepository;
import org.agh.backend.repository.OfficeRepository;
import org.agh.backend.service.DutyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/duties")
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class DutyController {

    private final DutyService dutyService;
    private final DoctorRepository doctorRepository;
    private final OfficeRepository officeRepository;

    public DutyController(
            DutyService dutyService,
            DoctorRepository doctorRepository,
            OfficeRepository officeRepository
    ) {
        this.dutyService = dutyService;
        this.doctorRepository = doctorRepository;
        this.officeRepository = officeRepository;
    }

    @PostMapping
    @Operation(
            summary = "Add a new duty",
            description = "Create a new duty for a doctor in a specific office and time range."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created a new duty"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Doctor or office not found"),
            @ApiResponse(responseCode = "409", description = "Time conflict for doctor or office")
    })
    public ResponseEntity<DutyDto> addDuty(
            @RequestBody DutyCreateDto dutyCreateDto
    ) {
        Doctor doctor = doctorRepository.findById(dutyCreateDto.getDoctorId())
                .orElse(null);
        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }

        Office office = officeRepository.findById(dutyCreateDto.getOfficeId())
                .orElse(null);
        if (office == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Duty duty = dutyService.createDuty(
                    doctor,
                    office,
                    dutyCreateDto.getStart(),
                    dutyCreateDto.getFinish()
            );

            return ResponseEntity.status(201).body(new DutyDto(duty));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a duty by ID",
            description = "Deletes a duty by its unique ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Duty not found")
    })
    public ResponseEntity<Void> deleteDuty(@PathVariable Long id) {
        try {
            dutyService.deleteDuty(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
