package com.careandcure.cac.controller;

import com.careandcure.cac.model.Appointment;
import com.careandcure.cac.model.Patient;
import com.careandcure.cac.model.Doctor;
import com.careandcure.cac.service.AppointmentService;
import com.careandcure.cac.service.PatientService;
import com.careandcure.cac.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("/patient/{patientId}/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    // Get all appointments for a specific patient
    // @GetMapping
    // public ResponseEntity<List<Appointment>> getAppointmentsForPatient(@PathVariable int patientId) {
    //     List<Appointment> appointments = appointmentService.getAppointmentsForPatient(patientId);
    //     if (appointments.isEmpty()) {
    //         return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    //     }
    //     return ResponseEntity.ok(appointments);
    // }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAppointmentsByPatientId(@PathVariable int patientId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByPatientId(patientId);
        if (appointments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appointments);
    }

    // Get an appointment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable int patientId, @PathVariable int id) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
        if (appointment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        return ResponseEntity.ok(appointment.get());
    }

    // Get appointments for a specific doctor
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getAppointmentsForDoctor(@PathVariable int patientId,
            @PathVariable int doctorId) {
        List<Appointment> appointments = appointmentService.getAppointmentsForDoctor(doctorId);
        if (appointments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(appointments);
    }

    // Create a new appointment
    @PostMapping("/schedule")
    public ResponseEntity<?> createAppointment(@PathVariable int patientId, @RequestBody Appointment appointment) {
        Patient patient = patientService.getPatientById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient with ID " + patientId + " not found"));

        if (appointment.getDoctor() == null || Objects.isNull(appointment.getDoctor().getDoctorId())) {
            return ResponseEntity.badRequest().body("Doctor information is missing or invalid.");
        }

        Doctor doctor = doctorService.getDoctorById(appointment.getDoctor().getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Doctor with ID " + appointment.getDoctor().getDoctorId() + " not found"));

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        Appointment savedAppointment = appointmentService.createAppointment(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAppointment);
    }

    // Update an appointment
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable int patientId, @PathVariable int id,
            @RequestBody Appointment updatedAppointment) {
        Patient patient = patientService.getPatientById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient with ID " + patientId + " not found"));

        Appointment existingAppointment = appointmentService.getAppointmentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment with ID " + id + " not found"));

        if (updatedAppointment.getDoctor() == null || Objects.isNull(updatedAppointment.getDoctor().getDoctorId())) {
            return ResponseEntity.badRequest().body("Doctor information is missing or invalid.");
        }

        Doctor doctor = doctorService.getDoctorById(updatedAppointment.getDoctor().getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Doctor with ID " + updatedAppointment.getDoctor().getDoctorId() + " not found"));

        existingAppointment.setDoctor(doctor);
        existingAppointment.setPatient(patient);
        existingAppointment.setAppointmentDate(updatedAppointment.getAppointmentDate());
        existingAppointment.setAppointmentTime(updatedAppointment.getAppointmentTime());
        existingAppointment.setReason(updatedAppointment.getReason());

        Appointment updated = appointmentService.updateAppointment(existingAppointment);
        return ResponseEntity.ok(updated);
    }

    // Cancel an appointment
    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancelAppointment(@PathVariable int patientId, @PathVariable int id,
            @RequestParam String reason) {
        appointmentService.getAppointmentById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Appointment with ID " + id + " not found for patient " + patientId));

        appointmentService.cancelAppointment(id, reason);
        return ResponseEntity.ok("Appointment with ID " + id + " has been cancelled for patient " + patientId +
                " for the following reason: " + reason);
    }

    // Delete an appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable int patientId, @PathVariable int id) {
        appointmentService.deleteAppointmentById(id);
        return ResponseEntity.ok("Appointment with ID " + id + " deleted successfully for patient " + patientId);
    }

    // Check if a time slot is available
    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkTimeSlotAvailability(@PathVariable int patientId,
            @RequestParam int doctorId,
            @RequestParam String date,
            @RequestParam String time) {
        LocalDate appointmentDate = LocalDate.parse(date);
        LocalTime appointmentTime = LocalTime.parse(time);
        boolean isAvailable = appointmentService.isTimeSlotAvailable(doctorId, appointmentDate, appointmentTime);
        return ResponseEntity.ok(isAvailable);
    }
}






// package com.careandcure.cac.controller;

// import com.careandcure.cac.model.AppointmentModel;
// import com.careandcure.cac.service.AppointmentService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;

// import java.time.LocalDate;
// import java.time.LocalTime;
// import java.util.List;

// @RestController
// @CrossOrigin(origins = "http://localhost:8081")
// @RequestMapping("/patient/{patientId}/appointments")
// public class AppointmentController {

// @Autowired
// private AppointmentService appointmentService;

// // Get all appointments
// @GetMapping
// public List<AppointmentModel> getAllAppointments() {
// return appointmentService.getAllAppointments();
// }

// // Get an appointment by ID
// @GetMapping("/{id}")
// public AppointmentModel getAppointmentById(@PathVariable int id) {
// return appointmentService.getAppointmentById(id)
// .orElseThrow(() -> new RuntimeException("Appointment with ID " + id + " not
// found"));
// }

// // Get appointments for a specific doctor
// @GetMapping("/doctor/{doctorId}")
// public List<AppointmentModel> getAppointmentsForDoctor(@PathVariable int
// doctorId) {
// return appointmentService.getAppointmentsForDoctor(doctorId);
// }

// // Create a new appointment
// @PostMapping("/schedule")
// public AppointmentModel createAppointment(@RequestBody AppointmentModel
// appointmentModel) {
// return appointmentService.createAppointment(appointmentModel);
// }

// // Update an appointment
// @PutMapping("/{id}")
// public AppointmentModel updateAppointment(@PathVariable int id, @RequestBody
// AppointmentModel updatedAppointment) {
// updatedAppointment.setAppointmentId(id);
// return appointmentService.updateAppointment(updatedAppointment);
// }

// // Cancel an appointment
// @PostMapping("/cancel/{id}")
// public String cancelAppointment(@PathVariable int id, @RequestParam String
// reason) {
// appointmentService.cancelAppointment(id, reason);
// return "Appointment with ID " + id + " has been cancelled for the following
// reason: " + reason;
// }

// // Delete an appointment
// @DeleteMapping("/{id}")
// public String deleteAppointment(@PathVariable int id) {
// appointmentService.deleteAppointmentById(id);
// return "Appointment with ID " + id + " deleted successfully.";
// }

// // Check if a time slot is available
// @GetMapping("/check-availability")
// public boolean checkTimeSlotAvailability(
// @RequestParam int doctorId,
// @RequestParam String date,
// @RequestParam String time) {
// LocalDate appointmentDate = LocalDate.parse(date);
// LocalTime appointmentTime = LocalTime.parse(time);
// return appointmentService.isTimeSlotAvailable(doctorId, appointmentDate,
// appointmentTime);
// }

// // Get all appointments for a specific patient
// @GetMapping("/patient/{patientId}")
// public List<AppointmentModel> getAppointmentsForPatient(@PathVariable int
// patientId) {
// return appointmentService.getAppointmentsForPatient(patientId);
// }
// }
