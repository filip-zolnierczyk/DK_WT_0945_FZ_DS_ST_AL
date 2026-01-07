package org.agh.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.agh.backend.dto.DutyDto;
import org.agh.backend.dto.OfficeCreateDto;
import org.agh.backend.dto.OfficeDto;
import org.agh.backend.model.Office;
import org.agh.backend.service.OfficeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/offices")
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class OfficeController {

    private final OfficeService officeService;

    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @GetMapping
    @Operation(
            summary = "Get all offices",
            description = "Retrieve a list of all offices in the system."
    )
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of offices",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OfficeDto.class)
                    )
            )
    )
    public ResponseEntity<List<OfficeDto>> getAllOffices() {
        List<OfficeDto> offices = officeService.getAllOffices()
                .stream()
                .map(OfficeDto::new)
                .toList();

        return ResponseEntity.ok(offices);
    }

    @PostMapping
    @Operation(
            summary = "Add a new office",
            description = "Create a new office in the system with the provided details."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully created a new office"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<OfficeDto> addOffice(
            @RequestBody OfficeCreateDto officeCreateDto
    ) {
        if (officeCreateDto.getName() == null || officeCreateDto.getAddress() == null) {
            return ResponseEntity.badRequest().build();
        }

        Office office = officeService.addOffice(
                officeCreateDto.getName(),
                officeCreateDto.getAddress(),
                officeCreateDto.getDescription()
        );

        return ResponseEntity.status(201).body(new OfficeDto(office));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete office by ID",
            description = "Delete a specific office from the system using its unique ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully deleted office"),
            @ApiResponse(responseCode = "404", description = "Office not found"),
            @ApiResponse(responseCode = "409", description = "Office has assigned duties")
    })
    public ResponseEntity<Void> deleteOffice(@PathVariable Long id) {
        try {
            boolean deleted = officeService.deleteOfficeByIdWithCheck(id);
            if (deleted) {
                return ResponseEntity.noContent().build(); // 204
            } else {
                return ResponseEntity.notFound().build(); // 404
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build(); // 409 Conflict – gabinet ma dyżury
        }
    }

    @GetMapping("/{id}/duties")
    @Operation(
            summary = "Get all duties in an office",
            description = "Retrieve a list of all duties assigned to a specific office."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved duties",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DutyDto.class))),
            @ApiResponse(responseCode = "404", description = "Office not found")
    })
    public ResponseEntity<List<DutyDto>> getDutiesOfOffice(@PathVariable Long id) {
        Office office = officeService.getAllOffices().stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (office == null) {
            return ResponseEntity.notFound().build();
        }

        List<DutyDto> duties = office.getDuties().stream()
                .map(d -> new DutyDto(d))
                .toList();

        return ResponseEntity.ok(duties);
    }

}
