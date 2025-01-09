package com.careandcure.cac.service;

import com.careandcure.cac.Exception.ResourceNotFoundException;
import com.careandcure.cac.model.Appointment;
import com.careandcure.cac.model.Doctor;
import com.careandcure.cac.model.Patient;
import com.careandcure.cac.repository.AppointmentRepository;
import com.careandcure.cac.repository.DoctorRepository;
import com.careandcure.cac.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public List<Appointment> getAppointmentsByPatientId(int patientId) throws ResourceNotFoundException {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));
        return appointmentRepository.findByPatient(patient);
    }

    public Optional<Appointment> getAppointmentById(int appointmentId) {
        return appointmentRepository.findById(appointmentId);
    }

    public List<Appointment> getAppointmentsForDoctor(int doctorId) throws ResourceNotFoundException {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));
        return appointmentRepository.findByDoctor(doctor);
    }

    public Appointment createAppointment(int patientId, Appointment appointment) throws ResourceNotFoundException {
        validateAppointmentDetails(appointment);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));

        Doctor doctor = doctorRepository.findById(appointment.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + appointment.getDoctorId()));

        if (!isTimeSlotAvailable(doctor.getDoctorId(), appointment.getAppointmentDate(), appointment.getAppointmentTime())) {
            throw new IllegalArgumentException("The selected time slot is already booked.");
        }

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(int id, Appointment updatedAppointment) throws ResourceNotFoundException {
        validateAppointmentDetails(updatedAppointment);

        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with ID " + id + " not found"));

        // Update fields
        existingAppointment.setAppointmentDate(updatedAppointment.getAppointmentDate());
        existingAppointment.setAppointmentTime(updatedAppointment.getAppointmentTime());
        existingAppointment.setStatus(updatedAppointment.getStatus());
        existingAppointment.setReason(updatedAppointment.getReason());

        if (updatedAppointment.getDoctorId() != 0) {
            Doctor doctor = doctorRepository.findById(updatedAppointment.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor with ID " + updatedAppointment.getDoctorId() + " not found"));
            existingAppointment.setDoctor(doctor);
        }

        return appointmentRepository.save(existingAppointment);
    }

    public void cancelAppointment(int id, String reason) throws ResourceNotFoundException {
        if (!StringUtils.hasText(reason)) {
            throw new IllegalArgumentException("Reason for cancellation cannot be empty.");
        }

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        appointment.setStatus("Cancelled");
        appointment.setReasonOfCancellation(reason);
        appointmentRepository.save(appointment);
    }

    public void deleteAppointmentById(int id) throws ResourceNotFoundException {
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Appointment not found with ID: " + id);
        }
        appointmentRepository.deleteById(id);
    }

    public boolean checkTimeSlotAvailability(int doctorId, String date, String time) throws ResourceNotFoundException {
        if (!StringUtils.hasText(date) || !StringUtils.hasText(time)) {
            throw new IllegalArgumentException("Date and time cannot be empty.");
        }

        LocalDate appointmentDate = LocalDate.parse(date.trim());
        LocalTime appointmentTime = LocalTime.parse(time.trim());

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));

        return !appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(doctor, appointmentDate, appointmentTime);
    }

    private boolean isTimeSlotAvailable(int doctorId, LocalDate appointmentDate, LocalTime appointmentTime) throws ResourceNotFoundException {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));
        return !appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(doctor, appointmentDate, appointmentTime);
    }

    private void validateAppointmentDetails(Appointment appointment) {
        if (appointment.getAppointmentDate() == null) {
            throw new IllegalArgumentException("Appointment date cannot be null.");
        }

        if (appointment.getAppointmentTime() == null) {
            throw new IllegalArgumentException("Appointment time cannot be null.");
        }

        if (!StringUtils.hasText(appointment.getStatus())) {
            throw new IllegalArgumentException("Appointment status cannot be empty.");
        }

        if (appointment.getDoctorId() == 0) {
            throw new IllegalArgumentException("Doctor ID cannot be zero.");
        }
    }
}
