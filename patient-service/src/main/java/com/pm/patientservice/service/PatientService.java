package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.kafka.KafkaProducer;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import jakarta.validation.constraints.Email;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    // Constructor
    public PatientService(PatientRepository patientRepository,
                          BillingServiceGrpcClient billingServiceGrpcClient,
                          KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    /**
     * Service function to get patients
     * @return list of DTOs containing patient data
     */
    public List<PatientResponseDTO> getPatients(){
        // Get patient and convert to DTO object
        List<Patient> patients = patientRepository.findAll();

        // For each patient in patients, call our toDTO function from our PatientMapper to convert to DTO
        return patients.stream()
                .map(PatientMapper::toDTO).toList();
    }

    /**
     * Service function for creating patient
     * @param patientRequestDTO DTO containing new patient data
     * @return DTO containing newly created patient data
     */
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        // Check for unique email
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email " + "already exists "
                    + patientRequestDTO.getEmail());
        }

        // Save newly created patient to DB
        Patient newPatient = patientRepository.save(PatientMapper.toPatient(patientRequestDTO));

        // Create billing account once we successfully add to DB
        billingServiceGrpcClient.createBillingAccount(
                newPatient.getId().toString(),
                newPatient.getName(),
                newPatient.getEmail());

        kafkaProducer.sendEvent(newPatient);

        return PatientMapper.toDTO(newPatient);
    }

    /**
     * Update existing patient data
     * @param id patient ID
     * @param patientRequestDTO DTO containing patient data
     * @return DTO containing updated patient data
     */
    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with ID " + id));

        // Check if email already exists if not the same id
        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),id)) {
            throw new EmailAlreadyExistsException(
                    "A patient with this email " + "already exists" + patientRequestDTO.getEmail()
            );
        }

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);

    }

    /**
     * Delete patient with id
     * @param id id of patient
     */
    @DeleteMapping
    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }

}
