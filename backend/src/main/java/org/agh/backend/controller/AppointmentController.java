package org.agh.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.agh.backend.dto.AppointmentCreateDto;
import org.agh.backend.dto.AppointmentListDto;
import org.agh.backend.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/appointments")
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class AppointmentController {

    AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/{dutyId}")
    @Operation(
            summary = "Get all appointments of a duty",
            description = "Retrieve a list of all appointments for a duty by its dutyId"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of appointments",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentListDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Duty not found"
            )
    })
    public ResponseEntity<List<AppointmentListDto>> getAppointmentsByDutyId(@PathVariable Long dutyId) {
        try {
            return ResponseEntity.ok(appointmentService.getAppointmentListByDutyId(dutyId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    @Operation(
            summary = "Add an appointment",
            description = "Add an appointment for a specific patient, duty and time."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully added an appointment"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "One of the parameters does not exists in the database."
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Patient is already on an appointment during the time"
            )
    })
    public ResponseEntity<Void> addAppointment(@RequestBody AppointmentCreateDto appointmentCreateDto) {
        try {
            appointmentService.addAppointment(appointmentCreateDto);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Cancel an appointment"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully canceled an appointment"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Appointment not found"
            )
    })
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        try {
            appointmentService.cancelAppointment(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
