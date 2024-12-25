package com.careandcure.cac.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int doctorId;

    @Column(name = "doctorName", nullable = false, length = 50)
    private String name;

    @Column(name = "specialization", length = 50)
    private String specialty;

    @Column(name = "qualification", length = 50)
    private String qualification;

    @Column(unique = true, nullable = false, length = 15)
    private String contactNumber;

    @Column(name = "emailId", unique = true, length = 100)
    private String emailId;

    @Column(length = 10)
    private String gender;

    @Column(length = 100)
    private String location;

    private Double consultationFees;
    private LocalDate dateOfJoining;
    private Boolean isSurgeon;
    private Integer yearsOfExperience;
    private Boolean status;

    @ElementCollection
    @CollectionTable(name = "doctor_availability", joinColumns = @JoinColumn(name = "doctor_id"))
    @MapKeyColumn(name = "day_of_week")
    @Column(name = "availability_slot")
    private Map<String, List<String>> availability;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Managed side of the relationship with Appointments
    private List<Appointment> appointments;


    }

