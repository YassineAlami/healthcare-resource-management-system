package ma.hrms.healthcareresourcemanagementsystem.service;

//import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ma.hrms.healthcareresourcemanagementsystem.dto.DoctorDTO;
import ma.hrms.healthcareresourcemanagementsystem.dto.PatientDTO;
import ma.hrms.healthcareresourcemanagementsystem.exception.ResourceNotFoundException;
import ma.hrms.healthcareresourcemanagementsystem.model.Doctor;
import ma.hrms.healthcareresourcemanagementsystem.model.Patient;
import ma.hrms.healthcareresourcemanagementsystem.model.Specialty;
import ma.hrms.healthcareresourcemanagementsystem.repository.DoctorRepository;
import ma.hrms.healthcareresourcemanagementsystem.repository.PatientRepository;
import ma.hrms.healthcareresourcemanagementsystem.repository.SpecialtyRepository;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional //(readOnly = true)  // Using readOnly since these are query methods
@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecialtyRepository specialtyRepository;
    private final ModelMapper modelMapper;
    private final ModelMapper patientModelMapper;
    private final PatientRepository patientRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, SpecialtyRepository specialtyRepository, ModelMapper modelMapper, @Qualifier("patientModelMapper") ModelMapper patientModelMapper, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.specialtyRepository = specialtyRepository;
        this.modelMapper = modelMapper;
        this.patientModelMapper = patientModelMapper;
        this.patientRepository = patientRepository;
    }

    @Override
    @Transactional
    public DoctorDTO saveOrUpdateDoctor(DoctorDTO doctorDTO) {
        try {
            // Step 1: Create Doctor entity
            Doctor doctor = modelMapper.map(doctorDTO, Doctor.class);
            log.debug("Initial doctor mapping: {}", doctor);

            // Step 2: Handle specialty if provided
            if (doctorDTO.getSpecialtyName() != null && !doctorDTO.getSpecialtyName().isBlank()) {
                try {
                    Specialty specialty = specialtyRepository.findByName(doctorDTO.getSpecialtyName())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Specialty not found with name: " + doctorDTO.getSpecialtyName()));
                    doctor.setSpecialty(specialty);
                    log.debug("Set specialty: {}", specialty.getName());
                } catch (Exception e) {
                    log.error("Error setting specialty", e);
                    throw new ValidationException("Error setting specialty: " + e.getMessage());
                }
            }

            // Step 3: Save the doctor
            try {
                doctor = doctorRepository.save(doctor);
                log.debug("Saved doctor: {}", doctor);
            } catch (Exception e) {
                log.error("Error saving doctor", e);
                throw new ValidationException("Error saving doctor: " + e.getMessage());
            }

            // Step 4: Map back to DTO
            try {
                DoctorDTO resultDTO = modelMapper.map(doctor, DoctorDTO.class);
                log.debug("Mapped to DTO: {}", resultDTO);
                return resultDTO;
            } catch (Exception e) {
                log.error("Error mapping to DTO", e);
                throw new ValidationException("Error creating response DTO: " + e.getMessage());
            }
        } catch (Exception e) {
            log.error("Unexpected error in saveOrUpdateDoctor", e);
            throw new ValidationException("Error processing doctor: " + e.getMessage());
        }
    }

    // Add a new method to assign specialty to an existing doctor
    @Override
    public DoctorDTO assignSpecialty(Long doctorId, String specialtyName) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));

        Specialty specialty = specialtyRepository.findByName(specialtyName)
                .orElseThrow(() -> new ResourceNotFoundException("Specialty not found with name: " + specialtyName));

        doctor.setSpecialty(specialty);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return modelMapper.map(savedDoctor, DoctorDTO.class);
    }

    @Override
    public Optional<DoctorDTO> findDoctorById(Long id) {
        return doctorRepository.findById(id)
                .map(doctor -> modelMapper.map(doctor, DoctorDTO.class));
    }

    @Override
    public Optional<DoctorDTO> findDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email)
                .map(doctor -> modelMapper.map(doctor, DoctorDTO.class));
    }

    @Override
    public List<DoctorDTO> findDoctorsBySpecialty(String specialtyName) {
        List<Doctor> doctors = doctorRepository.findBySpecialty_Name(specialtyName);
        return doctors.stream()
                .map(doctor -> modelMapper.map(doctor, DoctorDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorDTO> findAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctor -> modelMapper.map(doctor, DoctorDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public List<DoctorDTO> findDoctorsByName(String firstName, String lastName) {
        List<Doctor> doctors = doctorRepository.findByFirstNameAndLastName(firstName, lastName);
        return doctors.stream()
                .map(doctor -> modelMapper.map(doctor, DoctorDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    // Approach 1: Using the bidirectional relationship
    public List<PatientDTO> getPatientsByDoctor(Long doctorId) {
        log.debug("Fetching patients for doctor ID: {}", doctorId);

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));

        return doctor.getPatients().stream()
                .map(patient -> patientModelMapper.map(patient, PatientDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    // Approach 2: Using a dedicated repository method
    //NOT WORKING
    public List<PatientDTO> getPatientsByDoctorWithPagination(Long doctorId, Pageable pageable) {
        log.debug("Fetching patients for doctor ID: {} with pagination", doctorId);

        // First verify doctor exists
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with ID: " + doctorId);
        }

        Page<Patient> patientsPage = patientRepository.findByDoctorId(doctorId, pageable);

        return patientsPage.getContent().stream()
                .map(patient -> patientModelMapper.map(patient, PatientDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    // Optional: Method to get count of patients for a doctor
    public long getPatientCountForDoctor(Long doctorId) {
        log.debug("Getting patient count for doctor ID: {}", doctorId);

        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with ID: " + doctorId);
        }

        return patientRepository.countByDoctorId(doctorId);
    }



    @Override
    public boolean hasPatientWithName(Long doctorId, String firstName, String lastName) {
        log.debug("Searching for patient with name: {} {} under doctor ID: {}",
                firstName, lastName, doctorId);

        // First verify doctor exists
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));

        // Search for patient with case-insensitive name match
        return patientRepository.existsByDoctorIdAndFirstNameIgnoreCaseAndLastNameIgnoreCase(
                doctorId, firstName, lastName);
    }


    // Alternative method that returns patient details if found
    @Override
    public Optional<PatientDTO> findPatientByNameForDoctor(Long doctorId, String firstName, String lastName) {
        log.debug("Searching for patient details with name: {} {} under doctor ID: {}",
                firstName, lastName, doctorId);

        // Verify doctor exists
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with ID: " + doctorId);
        }

        return patientRepository
                .findByDoctorIdAndFirstNameIgnoreCaseAndLastNameIgnoreCase(doctorId, firstName, lastName)
                .map(patient -> patientModelMapper.map(patient, PatientDTO.class));
    }



}
