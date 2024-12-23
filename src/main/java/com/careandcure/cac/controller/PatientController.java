package com.careandcure.cac.controller;

import com.careandcure.cac.model.Patient;
import com.careandcure.cac.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:8081")
public class PatientController {

    @Autowired
    private PatientService patientService;

    // Get all patients
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    // Get a patient by ID
    @GetMapping("/{patientId}")
    public ResponseEntity<Patient> getPatientById(@PathVariable int patientId) {
        Optional<Patient> patient = patientService.getPatientById(patientId);
        if (patient.isPresent()) {
            return ResponseEntity.ok(patient.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Add a new patient
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient createdPatient = patientService.createPatient(patient);
        return ResponseEntity.ok(createdPatient);
    }

    // Update an existing patient
    @PutMapping("/{patientId}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable int patientId,
            @RequestBody Patient patient) {
        patient.setPatientId(patientId); // Ensure the ID matches the path variable

        try {
            Patient updatedPatient = patientService.updatePatient(patient);
            return ResponseEntity.ok(updatedPatient);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a patient by ID
    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deletePatient(@PathVariable int patientId) {
        try {
            patientService.deletePatient(patientId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get patients by name
    @GetMapping("/search")
    public ResponseEntity<List<Patient>> getPatientsByName(@RequestParam String name) {
        List<Patient> patients = patientService.getPatientsByName(name);
        return ResponseEntity.ok(patients);
    }

    // Get patients by age range
    @GetMapping("/age")
    public ResponseEntity<List<Patient>> getPatientsByAgeRange(
            @RequestParam int minAge,
            @RequestParam int maxAge) {
        List<Patient> patients = patientService.getPatientsByAgeRange(minAge, maxAge);
        return ResponseEntity.ok(patients);
    }

    // Get patients by gender
    @GetMapping("/gender")
    public ResponseEntity<List<Patient>> getPatientsByGender(@RequestParam String gender) {
        List<Patient> patients = patientService.getPatientsByGender(gender);
        return ResponseEntity.ok(patients);
    }

    // Get patient details for display
    @GetMapping("/{patientId}/details")
    public ResponseEntity<Patient> getPatientDetailsForDisplay(@PathVariable int patientId) {
        Optional<Patient> patient = patientService.getPatientDetailsForDisplay(patientId);
        if (patient.isPresent()) {
            return ResponseEntity.ok(patient.get());
        }
        return ResponseEntity.notFound().build();
    }
}
