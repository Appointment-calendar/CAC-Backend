package com.careandcure.cac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "doctor_timing")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorTiming {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int timingId;

    private LocalDate availableDate;
    private String dayOfWeek;
    private String timeOfDay;
    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonIgnore
    private Doctor doctor;
}
