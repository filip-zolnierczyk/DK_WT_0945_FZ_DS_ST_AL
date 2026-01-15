package org.agh.backend.controller;

import org.agh.backend.dto.AppointmentCreateDto;
import org.agh.backend.dto.EmptyAppointmentDto;
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
    public ResponseEntity<List<EmptyAppointmentDto>> getEmptyAppointments(@PathVariable Long dutyId) {
        try {
            return ResponseEntity.ok(appointmentService.getEmptyAppointments(dutyId));
        } catch (Exception e) {
            // TODO(Make specific)
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> addAppointment(@RequestBody AppointmentCreateDto appointmentCreateDto) {
        try {
            appointmentService.addAppointment(appointmentCreateDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // TODO(Use specific exceptions)
            return ResponseEntity.badRequest().build();
        }
    }

}
