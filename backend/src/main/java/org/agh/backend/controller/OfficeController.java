package org.agh.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
}
