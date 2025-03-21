package ma.hrms.healthcareresourcemanagementsystem.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ma.hrms.healthcareresourcemanagementsystem.config.PatientModelMapperConfig;
import ma.hrms.healthcareresourcemanagementsystem.dto.PatientDTO;
import ma.hrms.healthcareresourcemanagementsystem.exception.ResourceNotFoundException;
import ma.hrms.healthcareresourcemanagementsystem.model.Doctor;
import ma.hrms.healthcareresourcemanagementsystem.model.Patient;
import ma.hrms.healthcareresourcemanagementsystem.repository.DoctorRepository;
import ma.hrms.healthcareresourcemanagementsystem.repository.PatientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;
    private final ModelMapper patientModelMapper;


    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, DoctorRepository doctorRepository, ModelMapper modelMapper, PatientModelMapperConfig patientModelMapperConfig, ModelMapper patientModelMapper) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.modelMapper = modelMapper;
        this.patientModelMapper = patientModelMapper;
    }

    @Override
    @Transactional
    public PatientDTO saveOrUpdatePatient(PatientDTO patientDTO) {
        log.debug("Received PatientDTO: {}", patientDTO);

        // Step 1: Map PatientDTO to Patient
        Patient patient = patientModelMapper.map(patientDTO, Patient.class);
        log.debug("Mapped Patient entity: {}", patient);

        // Step 2: Handle doctor assignment if provided
        if (patientDTO.getDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(patientDTO.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
            patient.setDoctor(doctor);
            log.debug("Assigned Doctor to Patient: {}", doctor);
        } else {
            throw new ValidationException("Doctor ID must be provided");
        }

        // Step 3: Save Patient entity
        patient = patientRepository.save(patient);
        log.debug("Saved Patient entity: {}", patient);

        // Step 4: Map back to PatientDTO
        try {
            PatientDTO resultDTO = patientModelMapper.map(patient, PatientDTO.class);
            log.debug("Mapping successful");
            return resultDTO;
        } catch (Exception e) {
            log.error("Mapping failed for patient: {}", patient);
            log.error("Error: ", e);
        }
        return null;
    }




    @Override
    public Optional<PatientDTO> findPatientById(Long id) {
        return patientRepository.findById(id)
                .map(patient -> patientModelMapper.map(patient, PatientDTO.class));
    }

    @Override
    public List<PatientDTO> findPatientsByDoctorId(Long doctorId) {
        List<Patient> patients = patientRepository.findByDoctorId(doctorId);
        return patients.stream()
                .map(patient -> patientModelMapper.map(patient, PatientDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientDTO> findAllPatients() {
        return patientRepository.findAll().stream()
                .map(patient -> patientModelMapper.map(patient, PatientDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }







    @Override
    public PatientDTO assignPatientToDoctor(Long patientId, Long doctorId) {
        log.debug("Assigning patient ID: {} to doctor ID: {}", patientId, doctorId);

        // Find patient and doctor, throw exception if either not found
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));

        // Initialize lists if null to prevent NPE
        if (doctor.getPatients() == null) {
            doctor.setPatients(new ArrayList<>());
        }

        // Set up bidirectional relationship
        patient.setDoctor(doctor);
        doctor.getPatients().add(patient);

        // Save both entities
        patient = patientRepository.save(patient);
        doctorRepository.save(doctor); // This might not be needed due to cascade, but included for clarity

        log.debug("Successfully assigned patient to doctor. Patient: {}, Doctor: {}", patient, doctor);

        // Map and return updated patient
        return patientModelMapper.map(patient, PatientDTO.class);
    }

    @Override
    public PatientDTO removePatientFromDoctor(Long patientId) {
        log.debug("Removing doctor assignment from patient ID: {}", patientId);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));

        Doctor doctor = patient.getDoctor();
        if (doctor != null) {
            doctor.getPatients().remove(patient);
            patient.setDoctor(null);

            patient = patientRepository.save(patient);
            log.debug("Successfully removed doctor assignment from patient: {}", patient);
        }

        return patientModelMapper.map(patient, PatientDTO.class);
    }



}
