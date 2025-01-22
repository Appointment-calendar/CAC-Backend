package com.careandcure.cac.service;

import com.careandcure.cac.model.Availability;
import com.careandcure.cac.model.Availability.DayOfWeek;
import com.careandcure.cac.repository.AvailabilityRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AvailabilityService {

    private final AvailabilityRepository repository;

    public AvailabilityService(AvailabilityRepository repository) {
        this.repository = repository;
    }

    public Map<String, Boolean> getAvailableTimeSlots(DayOfWeek dayOfWeek, Long doctorId) {
        Optional<Availability> availabilityOpt = repository.findByDayOfWeekAndDoctorId(dayOfWeek, doctorId);
        return availabilityOpt.map(Availability::getTimeSlots)
                .orElseThrow(() -> new IllegalArgumentException("Availability not found for the given day and doctor."));
    }

    public void scheduleTimeSlot(DayOfWeek dayOfWeek, Long doctorId, String timeSlot) {
        Optional<Availability> availabilityOpt = repository.findByDayOfWeekAndDoctorId(dayOfWeek, doctorId);
        Availability availability = availabilityOpt.orElseThrow(() -> new IllegalArgumentException("Availability not found for the given day and doctor."));
        Map<String, Boolean> timeSlots = availability.getTimeSlots();
        if (timeSlots.containsKey(timeSlot) && timeSlots.get(timeSlot)) {
            timeSlots.put(timeSlot, false);
            repository.save(availability);
        } else {
            throw new IllegalArgumentException("Time slot is not available or doesn't exist.");
        }
    }

    public void cancelTimeSlot(DayOfWeek dayOfWeek, Long doctorId, String timeSlot) {
        Optional<Availability> availabilityOpt = repository.findByDayOfWeekAndDoctorId(dayOfWeek, doctorId);
        Availability availability = availabilityOpt.orElseThrow(() -> new IllegalArgumentException("Availability not found for the given day and doctor."));
        Map<String, Boolean> timeSlots = availability.getTimeSlots();
        if (timeSlots.containsKey(timeSlot)) {
            timeSlots.put(timeSlot, true);
            repository.save(availability);
        } else {
            throw new IllegalArgumentException("Time slot doesn't exist.");
        }
    }
}


















// package com.careandcure.cac.service;

// import com.careandcure.cac.model.Availability;
// import com.careandcure.cac.model.Availability.DayOfWeek;
// import com.careandcure.cac.repository.AvailabilityRepository;
// import org.springframework.stereotype.Service;

// import java.util.Map;
// import java.util.Optional;

// @Service
// public class AvailabilityService {

//     private final AvailabilityRepository repository;

//     public AvailabilityService(AvailabilityRepository repository) {
//         this.repository = repository;
//     }

//     // Fetch available time slots
//     public Map<String, Boolean> getAvailableTimeSlots(DayOfWeek dayOfWeek, Long doctorId) {
//         Optional<Availability> availability = repository.findByDayOfWeekAndDoctorId(dayOfWeek, doctorId);
//         if (availability.isPresent()) {
//             return availability.get().getTimeSlots();
//         } else {
//             throw new IllegalArgumentException("Availability not found for the given day and doctor.");
//         }
//     }

//     // public Map<String, Boolean> getAvailableTimeSlots(DayOfWeek dayOfWeek, Long doctorId) {
//     //     System.out.println("Fetching availability for day: " + dayOfWeek + " and doctorId: " + doctorId);
//     //     Optional<Availability> availability = repository.findByDayOfWeekAndDoctorId(dayOfWeek, doctorId);
//     //     if (availability.isPresent()) {
//     //         return availability.get().getTimeSlots();
//     //     } else {
//     //         throw new IllegalArgumentException("Availability not found for the given day and doctor.");
//     //     }
//     // }
    

//     // Schedule a time slot
//     public void scheduleTimeSlot(DayOfWeek dayOfWeek, Long doctorId, String timeSlot) {
//         Optional<Availability> availabilityOpt = repository.findByDayOfWeekAndDoctorId(dayOfWeek, doctorId);
//         if (availabilityOpt.isPresent()) {
//             Availability availability = availabilityOpt.get();
//             Map<String, Boolean> timeSlots = availability.getTimeSlots();
//             if (timeSlots.containsKey(timeSlot) && timeSlots.get(timeSlot)) {
//                 timeSlots.put(timeSlot, false); // Mark the time slot as not available
//                 repository.save(availability);
//             } else {
//                 throw new IllegalArgumentException("Time slot is not available or doesn't exist.");
//             }
//         } else {
//             throw new IllegalArgumentException("Availability not found for the given day and doctor.");
//         }
//     }

//     // Cancel a scheduled time slot
//     public void cancelTimeSlot(DayOfWeek dayOfWeek, Long doctorId, String timeSlot) {
//         Optional<Availability> availabilityOpt = repository.findByDayOfWeekAndDoctorId(dayOfWeek, doctorId);
//         if (availabilityOpt.isPresent()) {
//             Availability availability = availabilityOpt.get();
//             Map<String, Boolean> timeSlots = availability.getTimeSlots();
//             if (timeSlots.containsKey(timeSlot)) {
//                 timeSlots.put(timeSlot, true); // Mark the time slot as available
//                 repository.save(availability);
//             } else {
//                 throw new IllegalArgumentException("Time slot doesn't exist.");
//             }
//         } else {
//             throw new IllegalArgumentException("Availability not found for the given day and doctor.");
//         }
//     }
// }

