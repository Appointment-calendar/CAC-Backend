package com.careandcure.cac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "doctor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int doctorId;

    @NotNull(message = "Doctor name cannot be null")
    @Size(min = 3, max = 50, message = "Doctor name must be between 3 and 50 characters")
    @Column(name = "doctorName", nullable = false, length = 50)
    private String name;

    @Size(max = 50, message = "Specialization cannot exceed 50 characters")
    @Column(name = "specialization", length = 50)
    private String specialty;

    @Size(max = 50, message = "Qualification cannot exceed 50 characters")
    @Column(name = "qualification", length = 50)
    private String qualification;

    @NotNull(message = "Contact number cannot be null")
    @Size(min = 10, max = 15, message = "Contact number must be between 10 and 15 characters")
    @Column(unique = true, nullable = false, length = 15)
    private String contactNumber;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    @Column(name = "emailId", unique = true, length = 100)
    private String emailId;

    @Size(max = 10, message = "Gender cannot exceed 10 characters")
    @Column(length = 10)
    private String gender;

    @Size(max = 100, message = "Location cannot exceed 100 characters")
    @Column(length = 100)
    private String location;

    @Positive(message = "Consultation fees must be positive")
    private Double consultationFees;

    private LocalDate dateOfJoining;

    @NotNull(message = "Surgeon status cannot be null")
    private Boolean isSurgeon;

    @Positive(message = "Years of experience must be a positive number")
    private Integer yearsOfExperience;

    private Boolean status;

    @ElementCollection
    @CollectionTable(name = "doctor_availability", joinColumns = @JoinColumn(name = "doctor_id"))
    @MapKeyColumn(name = "day_of_week")
    @Column(name = "availability_slot")
    private Map<String, List<String>> availability;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Appointment> appointments;
}
