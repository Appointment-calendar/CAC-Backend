package com.careandcure.cac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

  @NotNull(message = "Appointment date cannot be null")
  @FutureOrPresent(message = "Appointment date must be today or in the future")
  private LocalDate appointmentDate;

  @NotNull(message = "Appointment time cannot be null")
  private LocalTime appointmentTime;

  @NotEmpty(message = "Status cannot be empty")
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
  private int doctorId; // Temporary field for deserialization
}
