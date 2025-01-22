package com.careandcure.cac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "doctor_id")
    private int doctorId;

    @Column(name = "doctor_name", nullable = false, length = 50)
    private String name;

    @Column(name = "specialization", length = 50)
    private String specialty;

    @Column(name = "qualification", length = 50)
    private String qualification;

    @Column(name = "contact_number", unique = true, nullable = false, length = 15)
    private String contactNumber;

    @Column(name = "email_id", unique = true, length = 100)
    private String emailId;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "consultation_fees")
    private Double consultationFees;

    @Column(name = "date_of_joining")
    private LocalDate dateOfJoining;

    @Column(name = "about", length = 500)
    private String about;

    @Column(name = "is_surgeon")
    private Boolean isSurgeon;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "languages", length = 100)
    private String languages;

    @Column(name = "status")
    private Boolean status;

    // Simplified Map handling for availability (Optional)
    @ElementCollection
    @CollectionTable(name = "doctor_availability", joinColumns = @JoinColumn(name = "doctor_id"))
    @MapKeyColumn(name = "day_of_week")
    @Column(name = "availability_slot")
    @JsonIgnore
    private Map<String, List<String>> availability;

    // One-to-many relationship with appointments (if needed)
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Appointment> appointments;

    // One-to-many relationship with timings
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoctorTiming> timings;

    // One-to-many relationship with reviews
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    public Doctor(Doctor doctor) {
    }
}
