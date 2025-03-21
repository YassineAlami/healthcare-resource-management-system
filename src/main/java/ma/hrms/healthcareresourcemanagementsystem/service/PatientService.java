package ma.hrms.healthcareresourcemanagementsystem.service;

import ma.hrms.healthcareresourcemanagementsystem.dto.PatientDTO;
import ma.hrms.healthcareresourcemanagementsystem.model.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {

    PatientDTO saveOrUpdatePatient(PatientDTO patientDTO);

    Optional<PatientDTO> findPatientById(Long id);

    List<PatientDTO> findPatientsByDoctorId(Long doctorId);

    List<PatientDTO> findAllPatients();

    void deletePatient(Long id);

    PatientDTO assignPatientToDoctor(Long patientId, Long doctorId);

    PatientDTO removePatientFromDoctor(Long patientId);

    }
