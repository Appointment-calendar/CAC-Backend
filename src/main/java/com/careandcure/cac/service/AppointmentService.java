package com.careandcure.cac.service;

import com.careandcure.cac.model.Appointment;
import com.careandcure.cac.model.Doctor;
import com.careandcure.cac.model.Patient;
import com.careandcure.cac.repository.AppointmentRepository;
import com.careandcure.cac.repository.DoctorRepository;
import com.careandcure.cac.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    // Get all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Get all appointments for a specific patient by PatientId

    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));
        return appointmentRepository.findByPatient(patient);
    }

    // Get appointments for a specific doctor
    public List<Appointment> getAppointmentsForDoctor(int doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor with ID " + doctorId + " not found"));
        return appointmentRepository.findByDoctor(doctor);
    }

    // Get an appointment by ID
    public Optional<Appointment> getAppointmentById(int appointmentId) {
        return appointmentRepository.findById(appointmentId);
    }

    
    // Create a new appointment
    public Appointment createAppointment(Appointment appointment) {
        // Ensure the time slot is available before saving the appointment
        boolean isAvailable = isTimeSlotAvailable(
                appointment.getDoctor().getDoctorId(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime());

        if (!isAvailable) {
            throw new RuntimeException("The selected time slot is already booked. Please choose another time.");
        }

        // Save the appointment if the time slot is available
        return appointmentRepository.save(appointment);
    }

    // Update an existing appointment
    public Appointment updateAppointment(Appointment appointment) {
        // Ensure that the appointment exists
        if (!appointmentRepository.existsById(appointment.getAppointmentId())) {
            throw new RuntimeException("Appointment with ID " + appointment.getAppointmentId() + " not found");
        }

        // Optionally validate time slot availability during update
        boolean isAvailable = isTimeSlotAvailable(
                appointment.getDoctor().getDoctorId(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime());

        if (!isAvailable) {
            throw new RuntimeException("The selected time slot is already booked. Please choose another time.");
        }

        // Save the updated appointment
        return appointmentRepository.save(appointment);
    }



    // Check if a time slot is available for a given doctor, date, and time
    public boolean isTimeSlotAvailable(int doctorId, LocalDate appointmentDate, LocalTime appointmentTime) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor with ID " + doctorId + " not found"));

        // Check if there is any appointment already booked for the same doctor, date,
        // and time
        return !appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(doctor, appointmentDate,
                appointmentTime);
    }


    // Cancel an appointment by ID
    public void cancelAppointment(int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment with ID " + appointmentId + " not found"));

        // Set the status to 'Cancelled' and save the appointment
        appointment.setStatus("Cancelled");
        appointmentRepository.save(appointment);
    }

    // Delete an appointment by ID
    public void deleteAppointmentById(int appointmentId) {
        if (!appointmentRepository.existsById(appointmentId)) {
            throw new RuntimeException("Appointment with ID " + appointmentId + " not found");
        }
        appointmentRepository.deleteById(appointmentId);
    }

    // Cancel an appointment by ID with a reason
    public void cancelAppointment(int appointmentId, String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment with ID " + appointmentId + " not found"));

        appointment.setStatus("Cancelled");
        appointment.setReasonOfCancellation(reason);
        appointmentRepository.save(appointment);
    }

    // // Get all appointments for a specific patient
    public List<Appointment> getAppointmentsForPatient(int patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient with ID " + patientId + " not found"));
        return appointmentRepository.findByPatient(patient);
    }

}
