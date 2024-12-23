package com.careandcure.cac.repository;

import com.careandcure.cac.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PatientRepository extends JpaRepository<Patient, Integer> {


    List<Patient> findByNameContainingIgnoreCase(String name);

    List<Patient> findByAgeBetween(int minAge, int maxAge);

    List<Patient> findByGenderIgnoreCase(String gender);
    
}
