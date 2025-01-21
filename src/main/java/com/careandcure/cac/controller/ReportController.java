package com.careandcure.cac.controller;

import com.careandcure.cac.model.Appointment;
import com.careandcure.cac.model.Doctor;
import com.careandcure.cac.model.Patient;
import com.careandcure.cac.service.AppointmentService;
import com.careandcure.cac.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    // Doctor Appointment Report: Appointments for a specific doctor within a time period
//    @GetMapping("/doctor/{doctorId}/appointments")
//    public ResponseEntity<List<Appointment>> getDoctorAppointments(@PathVariable int doctorId,
//                                                                   @RequestParam String startDate,
//                                                                   @RequestParam String endDate) {
//        LocalDate start = LocalDate.parse(startDate);
//        LocalDate end = LocalDate.parse(endDate);
//        List<Appointment> appointments = appointmentService.getDoctorAppointmentsInRange(doctorId, start, end);
//        return ResponseEntity.ok(appointments);
//    }
//
//    // Health Issue Appointment Report: Appointments by health issue category
//    @GetMapping("/appointments/health-issue")
//    public ResponseEntity<List<Appointment>> getAppointmentsByHealthIssue(@RequestParam String healthIssue,
//                                                                           @RequestParam String startDate,
//                                                                           @RequestParam String endDate) {
//        LocalDate start = LocalDate.parse(startDate);
//        LocalDate end = LocalDate.parse(endDate);
//        //List<Appointment> appointments = appointmentService.getAppointmentsByHealthIssue(healthIssue, start, end);
//        return ResponseEntity.ok(null);
//    }
//
//    // Doctor Availability Report: Shows doctor's availability for specific days
//    @GetMapping("/doctor/{doctorId}/availability")
//    public ResponseEntity<Map<String, List<String>>> getDoctorAvailabilityReport(@PathVariable int doctorId) {
//        Map<String, List<String>> availability = doctorService.getDoctorAvailability(doctorId);
//        return ResponseEntity.ok(availability);
//    }
//
//    // Most Frequently Booked Doctors Report
//    @GetMapping("/most-booked-doctors")
//    public ResponseEntity<List<Doctor>> getMostBookedDoctors(@RequestParam String startDate,
//                                                             @RequestParam String endDate) {
//        LocalDate start = LocalDate.parse(startDate);
//        LocalDate end = LocalDate.parse(endDate);
//        List<Doctor> mostBookedDoctors = doctorService.getMostBookedDoctors(start, end);
//        return ResponseEntity.ok(mostBookedDoctors);
//    }
//
//    // Daily Patient Appointment Report: Patients with appointments on a particular day
//    @GetMapping("/appointments/daily")
//    public ResponseEntity<List<Appointment>> getDailyPatientAppointments(@RequestParam String date) {
//        LocalDate appointmentDate = LocalDate.parse(date);
//        List<Appointment> appointments = appointmentService.getAppointmentsByDate(appointmentDate);
//        return ResponseEntity.ok(appointments);
//    }
//
//    // Patient No-Show Report: Patients who missed appointments
//    @GetMapping("/appointments/no-show")
//    public ResponseEntity<List<Patient>> getPatientNoShowReport(@RequestParam String startDate,
//                                                                @RequestParam String endDate) {
//        LocalDate start = LocalDate.parse(startDate);
//        LocalDate end = LocalDate.parse(endDate);
//        List<Patient> patientsNoShow = appointmentService.getNoShowPatients(start, end);
//        return ResponseEntity.ok(patientsNoShow);
//    }
}
