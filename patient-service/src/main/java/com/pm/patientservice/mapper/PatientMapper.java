package com.pm.patientservice.mapper;

import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.dto.PatientRequestDTO;

import java.time.LocalDate;

public class PatientMapper {
    // Convert Patient object to PatientResponseDTO
    public static PatientResponseDTO toDTO(Patient patient) {
        // Create DTO and set values
        PatientResponseDTO patientDTO = new PatientResponseDTO();
        patientDTO.setId(patient.getId().toString());
        patientDTO.setName(patient.getName());
        patientDTO.setAddress(patient.getAddress());
        patientDTO.setEmail(patient.getEmail());
        patientDTO.setDateOfBirth(patient.getDateOfBirth().toString());

        return patientDTO;
    }

    // Convert PatientRequestDTO to Patient model
    public static Patient toPatient(PatientRequestDTO patientRequestDTO) {
        Patient patient = new Patient();
        // Get and set values from the request DTO
        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        // Convert string to LocalDate
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(patientRequestDTO.getRegisteredDate()));

        return patient;

    }
}
