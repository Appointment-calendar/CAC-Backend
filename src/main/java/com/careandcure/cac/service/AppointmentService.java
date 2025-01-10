package com.careandcure.cac.service;

import com.careandcure.cac.model.Appointment;
import com.careandcure.cac.model.Doctor;
import com.careandcure.cac.model.Patient;
import com.careandcure.cac.repository.AppointmentRepository;
import com.careandcure.cac.repository.DoctorRepository;
import com.careandcure.cac.repository.PatientRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private EmailService emailService;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + patientId));
        return appointmentRepository.findByPatient(patient);
    }

    public List<Appointment> getAppointmentsForDoctor(int doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor with ID " + doctorId + " not found"));
        return appointmentRepository.findByDoctor(doctor);
    }

    public Optional<Appointment> getAppointmentById(int appointmentId) {
        return appointmentRepository.findById(appointmentId);
    }

    public Appointment createAppointment(Appointment appointment) throws MessagingException {
        boolean isAvailable = isTimeSlotAvailable(
                appointment.getDoctor().getDoctorId(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime());

        if (!isAvailable) {
            throw new IllegalStateException("The selected time slot is already booked. Please choose another time.");
        }

        if (appointment.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Appointment date cannot be in the past.");
        }

        appointment.setStatus("Scheduled");
        Appointment savedAppointment = appointmentRepository.save(appointment);

        sendAppointmentConfirmationEmail(savedAppointment);
        return savedAppointment;
    }

    public Appointment updateAppointment(Appointment appointment) throws MessagingException {
        if (!appointmentRepository.existsById(appointment.getAppointmentId())) {
            throw new IllegalArgumentException("Appointment with ID " + appointment.getAppointmentId() + " not found.");
        }

        boolean isAvailable = isTimeSlotAvailable(
                appointment.getDoctor().getDoctorId(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime());

        if(appointment.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Appointment date cannot be in the past.");
        }

        if (!isAvailable) {
            throw new IllegalStateException("The selected time slot is already booked. Please choose another time.");
        }

        Appointment updatedAppointment = appointmentRepository.save(appointment);

        sendAppointmentConfirmationEmail(updatedAppointment);
        return updatedAppointment;
    }

    public boolean isTimeSlotAvailable(int doctorId, LocalDate appointmentDate, LocalTime appointmentTime) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor with ID " + doctorId + " not found"));

        return !appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(doctor, appointmentDate,
                appointmentTime);
    }

    public void cancelAppointment(int appointmentId, String reason) throws MessagingException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment with ID " + appointmentId + " not found"));

        appointment.setStatus("Cancelled");
        appointment.setReasonOfCancellation(reason);
        appointmentRepository.save(appointment);

        sendAppointmentCancellationEmail(appointment);
    }

    public void deleteAppointmentById(int appointmentId) {
        if (!appointmentRepository.existsById(appointmentId)) {
            throw new IllegalArgumentException("Appointment with ID " + appointmentId + " not found.");
        }
        appointmentRepository.deleteById(appointmentId);
    }

    @Transactional
    public Appointment rescheduleAppointment(int appointmentId, LocalDate newDate, LocalTime newTime) throws MessagingException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment with ID " + appointmentId + " not found."));

        // if(appointment.getAppointmentDate().isBefore(LocalDate.now())) {
        //     throw new IllegalArgumentException("Appointment date cannot be in the past.");
        // }
        boolean isAvailable = isTimeSlotAvailable(appointment.getDoctor().getDoctorId(), newDate, newTime);
        if (!isAvailable) {
            throw new IllegalStateException("The selected time slot is already booked. Please choose another time.");
        }

        appointment.setAppointmentDate(newDate);
        appointment.setAppointmentTime(newTime);
        appointment.setStatus("Rescheduled");
        appointment.setReasonOfCancellation(null);

        Appointment updatedAppointment = appointmentRepository.save(appointment);
        sendAppointmentRescheduleEmail(updatedAppointment);
        return updatedAppointment;
    }

    private void sendAppointmentConfirmationEmail(Appointment appointment) throws MessagingException {
        emailService.sendAppointmentConfirmationEmail(
                appointment.getPatient().getEmail(),
                appointment.getPatient().getName(),
                appointment.getDoctor().getName(),
                appointment.getAppointmentDate().toString(),
                appointment.getAppointmentTime().toString());
    }

    private void sendAppointmentCancellationEmail(Appointment appointment) throws MessagingException {
        emailService.sendAppointmentCancellationEmail(
                appointment.getPatient().getEmail(),
                appointment.getPatient().getName(),
                appointment.getDoctor().getName(),
                appointment.getAppointmentDate().toString(),
                appointment.getAppointmentTime().toString(),
                appointment.getReasonOfCancellation());
    }

    private void sendAppointmentRescheduleEmail(Appointment appointment) throws MessagingException {
        emailService.sendAppointmentRescheduleEmail(
                appointment.getPatient().getEmail(),
                appointment.getPatient().getName(),
                appointment.getDoctor().getName(),
                appointment.getAppointmentDate().toString(),
                appointment.getAppointmentTime().toString());
    }
}






















// package com.careandcure.cac.service;

// import com.careandcure.cac.model.Appointment;
// import com.careandcure.cac.model.Doctor;
// import com.careandcure.cac.model.Patient;
// import com.careandcure.cac.repository.AppointmentRepository;
// import com.careandcure.cac.repository.DoctorRepository;
// import com.careandcure.cac.repository.PatientRepository;
// import jakarta.mail.MessagingException;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.time.LocalDate;
// import java.time.LocalTime;
// import java.util.List;
// import java.util.Optional;

// @Service
// public class AppointmentService {

//     @Autowired
//     private AppointmentRepository appointmentRepository;

//     @Autowired
//     private DoctorRepository doctorRepository;

//     @Autowired
//     private PatientRepository patientRepository;

//     @Autowired
//     private EmailService emailService;

//     // Get all appointments
//     public List<Appointment> getAllAppointments() {
//         return appointmentRepository.findAll();
//     }

//     // Get all appointments for a specific patient by PatientId
//     public List<Appointment> getAppointmentsByPatientId(int patientId) {
//         Patient patient = patientRepository.findById(patientId)
//                 .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));
//         return appointmentRepository.findByPatient(patient);
//     }

//     // Get appointments for a specific doctor
//     public List<Appointment> getAppointmentsForDoctor(int doctorId) {
//         Doctor doctor = doctorRepository.findById(doctorId)
//                 .orElseThrow(() -> new RuntimeException("Doctor with ID " + doctorId + " not found"));
//         return appointmentRepository.findByDoctor(doctor);
//     }

//     // Get an appointment by ID
//     public Optional<Appointment> getAppointmentById(int appointmentId) {
//         return appointmentRepository.findById(appointmentId);
//     }

//     // Create a new appointment
//     public Appointment createAppointment(Appointment appointment) throws MessagingException {
//         // Ensure the time slot is available before saving the appointment
//         boolean isAvailable = isTimeSlotAvailable(
//                 appointment.getDoctor().getDoctorId(),
//                 appointment.getAppointmentDate(),
//                 appointment.getAppointmentTime());

//         if (!isAvailable) {
//             throw new RuntimeException("The selected time slot is already booked. Please choose another time.");
//         }



//         appointment.setStatus("Scheduled");  // Ensure the status is set correctly.
//          // Clear any cancellation reason
//         return  appointmentRepository.save(appointment);

//     }

//     // Update an existing appointment
//     public Appointment updateAppointment(Appointment appointment) throws MessagingException {
//         // Ensure that the appointment exists
//         if (!appointmentRepository.existsById(appointment.getAppointmentId())) {
//             throw new RuntimeException("Appointment with ID " + appointment.getAppointmentId() + " not found");
//         }

//         // Optionally validate time slot availability during update
//         boolean isAvailable = isTimeSlotAvailable(
//                 appointment.getDoctor().getDoctorId(),
//                 appointment.getAppointmentDate(),
//                 appointment.getAppointmentTime());

//         if (!isAvailable) {
//             throw new RuntimeException("The selected time slot is already booked. Please choose another time.");
//         }

//         // Save the updated appointment
//         Appointment updatedAppointment = appointmentRepository.save(appointment);

//         // Send updated appointment confirmation email
//         sendAppointmentConfirmationEmail(updatedAppointment);

//         return updatedAppointment;
//     }

//     // Check if a time slot is available for a given doctor, date, and time
//     public boolean isTimeSlotAvailable(int doctorId, LocalDate appointmentDate, LocalTime appointmentTime) {
//         Doctor doctor = doctorRepository.findById(doctorId)
//                 .orElseThrow(() -> new RuntimeException("Doctor with ID " + doctorId + " not found"));

//         // Check if there is any appointment already booked for the same doctor, date,
//         // and time
//         return !appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTime(doctor, appointmentDate,
//                 appointmentTime);
//     }

//     // Cancel an appointment by ID
//     public void cancelAppointment(int appointmentId, String reason) throws MessagingException {
//         Appointment appointment = appointmentRepository.findById(appointmentId)
//                 .orElseThrow(() -> new RuntimeException("Appointment with ID " + appointmentId + " not found"));

//         appointment.setStatus("Cancelled");
//         appointment.setReasonOfCancellation(reason);
//         appointmentRepository.save(appointment);

//         // Send cancellation email
//         sendAppointmentCancellationEmail(appointment);
//     }

//     // Delete an appointment by ID
//     public void deleteAppointmentById(int appointmentId) {
//         if (!appointmentRepository.existsById(appointmentId)) {
//             throw new RuntimeException("Appointment with ID " + appointmentId + " not found");
//         }
//         appointmentRepository.deleteById(appointmentId);
//     }

//     // Call EmailService to send confirmation email
//     private void sendAppointmentConfirmationEmail(Appointment appointment) throws MessagingException {
//         String patientEmail = appointment.getPatient().getEmail();
//         String patientName = appointment.getPatient().getName();
//         String doctorName = appointment.getDoctor().getName();
//         String appointmentDate = appointment.getAppointmentDate().toString();
//         String appointmentTime = appointment.getAppointmentTime().toString();

//         emailService.sendAppointmentConfirmationEmail(patientEmail, patientName, doctorName, appointmentDate, appointmentTime);
//     }

//     // Call EmailService to send cancellation email
//     private void sendAppointmentCancellationEmail(Appointment appointment) throws MessagingException {
//         String patientEmail = appointment.getPatient().getEmail();
//         String patientName = appointment.getPatient().getName();
//         String doctorName = appointment.getDoctor().getName();
//         String appointmentDate = appointment.getAppointmentDate().toString();
//         String appointmentTime = appointment.getAppointmentTime().toString();
//         String reason = appointment.getReasonOfCancellation();

//         emailService.sendAppointmentCancellationEmail(patientEmail, patientName, doctorName, appointmentDate, appointmentTime, reason);
//     }

//     @Transactional
//     public Appointment rescheduleAppointment(int appointmentId, LocalDate newDate, LocalTime newTime) throws MessagingException {
//         Appointment appointment = appointmentRepository.findById(appointmentId)
//                 .orElseThrow(() -> new RuntimeException("Appointment with ID " + appointmentId + " not found"));

//         boolean isAvailable = isTimeSlotAvailable(appointment.getDoctor().getDoctorId(), newDate, newTime);
//         if (!isAvailable) {
//             throw new RuntimeException("The selected time slot is already booked. Please choose another time.");
//         }

//         // Set status correctly before saving the appointment
//         appointment.setAppointmentDate(newDate);
//         appointment.setAppointmentTime(newTime);
//         appointment.setStatus("Scheduled");  // Ensure the status is set correctly.
//         appointment.setReasonOfCancellation(null);  // Clear any cancellation reason
//         Appointment updatedAppointment = appointmentRepository.save(appointment);

//         sendAppointmentRescheduleEmail(updatedAppointment);
//         return updatedAppointment;
//     }

//     private void sendAppointmentRescheduleEmail(Appointment appointment) throws MessagingException {
//         String patientEmail = appointment.getPatient().getEmail();
//         String patientName = appointment.getPatient().getName();
//         String doctorName = appointment.getDoctor().getName();
//         String appointmentDate = appointment.getAppointmentDate().toString();
//         String appointmentTime = appointment.getAppointmentTime().toString();

//         emailService.sendAppointmentRescheduleEmail(patientEmail, patientName, doctorName, appointmentDate, appointmentTime);
//     }
// }
