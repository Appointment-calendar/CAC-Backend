package com.careandcure.cac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.careandcure.cac.model.Appointment;
import com.careandcure.cac.model.Doctor;
import com.careandcure.cac.model.Patient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByDoctor(Doctor doctor);

    List<Appointment> findByPatient(Patient patient);
    boolean existsByDoctorAndAppointmentDateAndAppointmentTime(Doctor doctor, LocalDate date, LocalTime time);
}
