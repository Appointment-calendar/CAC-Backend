package com.careandcure.cac.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctor_availability")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Unique ID for each availability entry

    @ManyToOne(fetch = FetchType.LAZY) // Many availability slots belong to one doctor
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "day_of_week", nullable = false, length = 20)
    private String dayOfWeek;

    @Column(name = "availability_slot", nullable = false, length = 255)
    private String availabilitySlot; // Comma-separated string of time slots
}

