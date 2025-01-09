package com.careandcure.cac.controller;

import com.careandcure.cac.Exception.ResourceNotFoundException;
import com.careandcure.cac.model.Appointment;
import com.careandcure.cac.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // Get all appointments for a specific patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatientId(@PathVariable int patientId) throws ResourceNotFoundException {
        List<Appointment> appointments = appointmentService.getAppointmentsByPatientId(patientId);
        return appointments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(appointments);
    }

    // Get an appointment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable int id) {
        return appointmentService.getAppointmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Get appointments for a specific doctor
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getAppointmentsForDoctor(@PathVariable int doctorId) throws ResourceNotFoundException {
        List<Appointment> appointments = appointmentService.getAppointmentsForDoctor(doctorId);
        return appointments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(appointments);
    }

    // Schedule a new appointment
    @PostMapping("/schedule/{patientId}")
    public ResponseEntity<Appointment> createAppointment(
            @PathVariable int patientId,
            @Valid @RequestBody Appointment appointment) throws ResourceNotFoundException { // Add @Valid here
        Appointment savedAppointment = appointmentService.createAppointment(patientId, appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAppointment);
    }

    // Update an existing appointment
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable int id,
            @Valid @RequestBody Appointment updatedAppointment) throws ResourceNotFoundException { // Add @Valid here
        Appointment appointment = appointmentService.updateAppointment(id, updatedAppointment);
        return ResponseEntity.ok(appointment);
    }

    // Cancel an appointment
    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelAppointment(@PathVariable int id, @RequestParam String reason) throws ResourceNotFoundException {
        appointmentService.cancelAppointment(id, reason);
        return ResponseEntity.ok("Appointment with ID " + id + " has been cancelled for the following reason: " + reason);
    }

    // Delete an appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable int id) throws ResourceNotFoundException {
        appointmentService.deleteAppointmentById(id);
        return ResponseEntity.ok("Appointment with ID " + id + " deleted successfully");
    }

    // Check if a time slot is available for a doctor
    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkTimeSlotAvailability(
            @RequestParam int doctorId,
            @RequestParam String date,
            @RequestParam String time) throws ResourceNotFoundException {
        boolean isAvailable = appointmentService.checkTimeSlotAvailability(doctorId, date, time);
        return ResponseEntity.ok(isAvailable);
    }
}
