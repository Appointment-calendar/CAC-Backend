package com.careandcure.cac.controller;
import com.careandcure.cac.dto.DoctorDTO;
import com.careandcure.cac.model.Doctor;
import com.careandcure.cac.model.DoctorTiming;
import com.careandcure.cac.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://localhost:8081")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<Doctor>> getDoctors() {
        List<Doctor> doctors = doctorService.findAll();
        return ResponseEntity.ok(doctors);
    }

    // Get a doctor by ID
    @GetMapping("/{doctorId}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable int doctorId) {
        Optional<Doctor> doctor = doctorService.getDoctorById(doctorId);
        if (doctor.isPresent()) {
            Doctor doctorDTO =doctor.get();
            return ResponseEntity.ok(doctorDTO);
        }
        return ResponseEntity.notFound().build();
    }


    // Add a new doctor
    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor createdDoctor = doctorService.createDoctor(doctor);
        return ResponseEntity.ok(createdDoctor);
    }

    // Update doctor details
    @PutMapping("/{doctorId}")
    public ResponseEntity<Doctor> updateDoctor(
            @PathVariable int doctorId,
            @RequestBody Doctor doctor) {
        // Ensure the doctor ID matches the path variable
        doctor.setDoctorId(doctorId);

        try {
            Doctor updatedDoctor = doctorService.updateDoctor(doctor);
            return ResponseEntity.ok(updatedDoctor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a doctor by ID
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable int doctorId) {
        try {
            doctorService.deleteDoctor(doctorId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get available doctors
    @GetMapping("/available")
    public ResponseEntity<List<Doctor>> getAvailableDoctors() {
        List<Doctor> availableDoctors = doctorService.getAvailableDoctors();
        return ResponseEntity.ok(availableDoctors);
    }

    // Get doctor's availability by ID
    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<Map<String, List<String>>> getDoctorAvailability(@PathVariable int doctorId) {
        try {
            Map<String, List<String>> availability = doctorService.getDoctorAvailability(doctorId);
            return ResponseEntity.ok(availability);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Fetch doctor name and availability for display
    @GetMapping("/{doctorId}/details")
    public ResponseEntity<Map<String, Object>> getDoctorDetailsForDisplay(@PathVariable int doctorId) {
        try {
            Map<String, Object> doctorDetails = doctorService.getDoctorDetailsForDisplay(doctorId);
            return ResponseEntity.ok(doctorDetails);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get doctors by specialization
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialization(@PathVariable String specialization) {
        List<Doctor> doctors = doctorService.getDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(doctors);
    }
    @GetMapping("/search-doctors")
    public ResponseEntity<List<Doctor>> searchDoctors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) Integer experience,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String languages) {

        List<Doctor> doctors = doctorService.findDoctors(name, specialization, experience, gender, languages);
        return ResponseEntity.ok(doctors);
    }
    @GetMapping("/timings/{id}")
    public  List<DoctorTiming> doctorTimings(@PathVariable int id){
        List<DoctorTiming> doctorTimings=doctorService.getDoctorById(id).get().getTimings();
        return doctorTimings;
    }

}
