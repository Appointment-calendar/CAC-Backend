package com.careandcure.cac.repository;

import com.careandcure.cac.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    List<Doctor> findByStatus(Boolean status);

    List<Doctor> findBySpecialty(String specialty);
}
