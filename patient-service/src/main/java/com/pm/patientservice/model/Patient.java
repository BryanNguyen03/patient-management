package com.pm.patientservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    private String address;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    private LocalDate registeredDate;

    // ###### GETTERS AND SETTERS ######
    // Get id of patient
    public UUID getId() {
        return id;
    }

    // Set id of patient
    public void setId(UUID id) {
        this.id = id;
    }

    // Get name of patient
    public String getName() {
        return name;
    }

    // Set name for patient
    public void setName(String name) {
        this.name = name;
    }

    // get email of patient
    public String getEmail() {
        return email;
    }

    // set email for patient
    public void setEmail(String email) {
        this.email = email;
    }

    // get address of patient
    public String getAddress() {
        return address;
    }

    // set address of patient
    public void setAddress(String address) {
        this.address = address;
    }

    // get dob for patient
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    // set dob for patient
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // get register date for patient
    public LocalDate getRegisteredDate() {
        return registeredDate;
    }

    // set register date for patient
    public void setRegisteredDate(LocalDate registeredDate) {
        this.registeredDate = registeredDate;
    }


}
