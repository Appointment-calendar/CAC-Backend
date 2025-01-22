package com.careandcure.cac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "review")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonIgnore
    private Doctor doctor;

    @Column(name = "patient_name", nullable = false, length = 50)
    private String patientName;

    @Column(name = "rating", nullable = false)
    private int rating; // e.g., 1-5 stars

    @Column(name = "comments", length = 500)
    private String comments;
}
