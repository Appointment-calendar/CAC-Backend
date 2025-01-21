package com.careandcure.cac.repository;

import com.careandcure.cac.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    List<Doctor> findByStatus(Boolean status);

    List<Doctor> findBySpecialty(String specialty);

    // List<Doctor> findMostBookedDoctors(LocalDate startDate, LocalDate endDate);
    @Query("SELECT d FROM Doctor d WHERE " +
            "(:name IS NULL OR d.name LIKE %:name%) AND " +
            "(:specialization IS NULL OR d.specialty = :specialization) AND " +
            "(:experience IS NULL OR d.yearsOfExperience >= :experience) AND " +
            "(:gender IS NULL OR d.gender = :gender) AND " +
            "(:languages IS NULL OR d.languages LIKE %:languages%)")
    List<Doctor> findDoctors(@Param("name") String name,
                             @Param("specialization") String specialization,
                             @Param("experience") Integer experience,
                             @Param("gender") String gender,
                             @Param("languages") String languages);


}
