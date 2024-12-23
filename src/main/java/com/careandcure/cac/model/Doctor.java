package com.careandcure.cac.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
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


    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getConsultationFees() {
        return consultationFees;
    }

    public void setConsultationFees(Double consultationFees) {
        this.consultationFees = consultationFees;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public Boolean isSurgeon() {
        return isSurgeon;
    }

    public void setSurgeon(Boolean isSurgeon) {
        this.isSurgeon = isSurgeon;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Map<String, List<String>> getAvailability() {
        return availability;
    }

    public void setAvailability(Map<String, List<String>> availability) {
        this.availability = availability;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}

