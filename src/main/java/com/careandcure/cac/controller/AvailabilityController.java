package com.careandcure.cac.controller;

import com.careandcure.cac.model.Availability.DayOfWeek;
import com.careandcure.cac.service.AvailabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    private final AvailabilityService service;

    public AvailabilityController(AvailabilityService service) {
        this.service = service;
    }

    /**
     * GET /api/availability/time-slots
     * Fetch available time slots for a specific day and doctor.
     */
    @GetMapping("/time-slots")
    public ResponseEntity<Map<String, Boolean>> getAvailableTimeSlots(
            @RequestParam DayOfWeek dayOfWeek,
            @RequestParam Long doctorId) {
        Map<String, Boolean> availableSlots = service.getAvailableTimeSlots(dayOfWeek, doctorId);
        return ResponseEntity.ok(availableSlots);
    }

    /**
     * POST /api/availability/schedule
     * Schedule a time slot (mark it as not available).
     */
    @PostMapping("/schedule")
    public ResponseEntity<String> scheduleTimeSlot(
            @RequestParam DayOfWeek dayOfWeek,
            @RequestParam Long doctorId,
            @RequestParam String timeSlot) {
        service.scheduleTimeSlot(dayOfWeek, doctorId, timeSlot);
        return ResponseEntity.ok("Time slot scheduled successfully.");
    }

    /**
     * POST /api/availability/cancel
     * Cancel a scheduled time slot (mark it as available).
     */
    @PostMapping("/cancel")
    public ResponseEntity<String> cancelTimeSlot(
            @RequestParam DayOfWeek dayOfWeek,
            @RequestParam Long doctorId,
            @RequestParam String timeSlot) {
        service.cancelTimeSlot(dayOfWeek, doctorId, timeSlot);
        return ResponseEntity.ok("Time slot canceled successfully.");
    }
}

