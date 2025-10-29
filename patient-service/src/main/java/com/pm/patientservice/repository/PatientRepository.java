package com.pm.patientservice.repository;

import org.springframework.stereotype.Repository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pm.patientservice.model.Patient;

@Repository
// Takes in Patient as well as their ID
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, UUID id);
}
