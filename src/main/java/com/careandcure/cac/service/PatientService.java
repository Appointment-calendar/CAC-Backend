package com.careandcure.cac.service;

import com.careandcure.cac.model.Patient;
import com.careandcure.cac.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    // Fetch all patients
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // Fetch a patient by ID
    public Optional<Patient> getPatientById(int patientId) {
        return patientRepository.findById(patientId);
    }

    // Add a new patient
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    // Update an existing patient
    public Patient updatePatient(Patient patient) {
        if (!patientRepository.existsById(patient.getPatientId())) {
            throw new RuntimeException("Patient with ID " + patient.getPatientId() + " not found");
        }
        return patientRepository.save(patient);
    }

    // Delete a patient
    public void deletePatient(int patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new RuntimeException("Patient with ID " + patientId + " not found");
        }
        patientRepository.deleteById(patientId);
    }

    // Fetch patients by name
    public List<Patient> getPatientsByName(String name) {
        return patientRepository.findByNameContainingIgnoreCase(name);
    }

    // Fetch patients by age range
    public List<Patient> getPatientsByAgeRange(int minAge, int maxAge) {
        return patientRepository.findByAgeBetween(minAge, maxAge);
    }

    // Fetch patients by gender
    public List<Patient> getPatientsByGender(String gender) {
        return patientRepository.findByGenderIgnoreCase(gender);
    }

    // Get patient details for display
    public Optional<Patient> getPatientDetailsForDisplay(int patientId) {
        return patientRepository.findById(patientId);
    }
}
