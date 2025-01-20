package com.careandcure.cac.repository;

import com.careandcure.cac.model.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Integer> {
    
    // Fetch availability for a specific doctor on a specific day of the week
    List<DoctorAvailability> findByDoctorDoctorIdAndDayOfWeek(int doctorId, String dayOfWeek);
}
