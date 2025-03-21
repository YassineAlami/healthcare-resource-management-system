package ma.hrms.healthcareresourcemanagementsystem.service;

import ma.hrms.healthcareresourcemanagementsystem.dto.DoctorDTO;
import ma.hrms.healthcareresourcemanagementsystem.dto.PatientDTO;
import ma.hrms.healthcareresourcemanagementsystem.model.Doctor;
import ma.hrms.healthcareresourcemanagementsystem.model.Specialty;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DoctorService {

    DoctorDTO saveOrUpdateDoctor(DoctorDTO doctorDTO);

    Optional<DoctorDTO> findDoctorById(Long id);

    Optional<DoctorDTO> findDoctorByEmail(String email);

    List<DoctorDTO> findDoctorsBySpecialty(String specialtyName);

    List<DoctorDTO> findAllDoctors();

    void deleteDoctor(Long id);

    List<DoctorDTO> findDoctorsByName(String firstName, String lastName);

    DoctorDTO assignSpecialty(Long doctorId, String specialtyName);



    List<PatientDTO> getPatientsByDoctor(Long doctorId);
    List<PatientDTO> getPatientsByDoctorWithPagination(Long doctorId, Pageable pageable);
    long getPatientCountForDoctor(Long doctorId);



    boolean hasPatientWithName(Long doctorId, String firstName, String lastName);
    Optional<PatientDTO> findPatientByNameForDoctor(Long doctorId, String firstName, String lastName);


    }
