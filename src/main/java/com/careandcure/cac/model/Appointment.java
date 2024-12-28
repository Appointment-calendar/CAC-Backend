package com.careandcure.cac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int appointmentId;

  private LocalDate appointmentDate;
  private LocalTime appointmentTime;
  private String status;
  private String reason;
  private String reasonOfCancellation;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "patient_id", nullable = false)
  @JsonIgnore
  private Patient patient;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "doctor_id", nullable = false)
  @JsonIgnore
  private Doctor doctor;

  @Transient
  private Integer doctorId; // Change from int to Integer to allow null values
}

