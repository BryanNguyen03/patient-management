package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    // Constructor
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // Get patients and convert to DTO
    public List<PatientResponseDTO> getPatients(){
        // Get patient and convert to DTO object
        List<Patient> patients = patientRepository.findAll();

        // For each patient in patients, call our toDTO function from our PatientMapper to convert to DTO
        return patients.stream()
                .map(PatientMapper::toDTO).toList();
    }
}
