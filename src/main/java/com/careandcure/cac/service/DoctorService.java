package com.careandcure.cac.service;

import com.careandcure.cac.model.Appointment;
import com.careandcure.cac.model.Doctor;
import com.careandcure.cac.model.DoctorAvailability;
import com.careandcure.cac.repository.AppointmentRepository;
import com.careandcure.cac.repository.DoctorAvailabilityRepository;
import com.careandcure.cac.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private DoctorAvailabilityRepository doctorAvailabilityRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    // Fetch all doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    // Fetch a doctor by ID
    public Optional<Doctor> getDoctorById(int doctorId) {
        return doctorRepository.findById(doctorId);
    }

    // Add a new doctor
    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }
    
 // Method to get available time slots for a doctor on a given date
    public Map<String, List<String>> getAvailableTimeSlots(int doctorId, LocalDate appointmentDate) {
        // Find the day of the week for the given appointment date
        String dayOfWeek = appointmentDate.getDayOfWeek().name();

        // Fetch doctor's availability for that day
        List<DoctorAvailability> doctorAvailabilityList = doctorAvailabilityRepository.findByDoctorDoctorIdAndDayOfWeek(doctorId, dayOfWeek);

        // Fetch already booked appointments for the doctor on the given date
        List<Appointment> bookedAppointments = appointmentRepository.findByDoctorDoctorIdAndAppointmentDate(doctorId, appointmentDate);

        // Get booked time slots from the appointments
        Set<String> bookedTimeSlots = bookedAppointments.stream()
                .map(appointment -> appointment.getAppointmentTime().toString()) // Assuming appointmentTime is stored as LocalTime
                .collect(Collectors.toSet());

        // Filter out booked slots from the available ones
        Map<String, List<String>> availableTimeSlots = new HashMap<>();
        for (DoctorAvailability availability : doctorAvailabilityList) {
            List<String> slots = Arrays.asList(availability.getAvailabilitySlot().split(","));
            // Filter out booked time slots
            List<String> availableSlots = slots.stream()
                    .filter(slot -> !bookedTimeSlots.contains(slot))
                    .collect(Collectors.toList());
            availableTimeSlots.put(availability.getDayOfWeek(), availableSlots);
        }

        return availableTimeSlots;
    }
    
    // Update doctor details
    public Doctor updateDoctor(Doctor doctor) {
        if (!doctorRepository.existsById(doctor.getDoctorId())) {
            throw new RuntimeException("Doctor with ID " + doctor.getDoctorId() + " not found");
        }
        return doctorRepository.save(doctor);
    }

    // Delete a doctor
    public void deleteDoctor(int doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new RuntimeException("Doctor with ID " + doctorId + " not found");
        }
        doctorRepository.deleteById(doctorId);
    }

    // Get available doctors
    public List<Doctor> getAvailableDoctors() {
        return doctorRepository.findByStatus(true); // Assuming 'true' indicates availability
    }

    // Fetch doctor's availability by ID
    public List<?> getDoctorAvailability(int doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor with ID " + doctorId + " not found"));
        return doctor.getAvailability();
    }

    // Fetch doctor name and availability for the frontend
    public Map<String, Object> getDoctorDetailsForDisplay(int doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor with ID " + doctorId + " not found"));

        Map<String, Object> doctorDetails = new HashMap<>();
        doctorDetails.put("doctorId", doctor.getDoctorId());
        doctorDetails.put("name", doctor.getName());
        doctorDetails.put("specialty", doctor.getSpecialty());
        doctorDetails.put("availability", doctor.getAvailability());
        doctorDetails.put("status", doctor.getStatus());

        return doctorDetails;
    }

    // Fetch doctors by specialization
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialty(specialization);
    }
}
