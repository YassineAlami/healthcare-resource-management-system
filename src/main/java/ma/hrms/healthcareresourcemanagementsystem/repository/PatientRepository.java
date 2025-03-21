package ma.hrms.healthcareresourcemanagementsystem.repository;

import ma.hrms.healthcareresourcemanagementsystem.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Find patients by doctor (This shows a reverse lookup from patient to doctor)
    List<Patient> findByDoctorId(Long doctorId);

    // Find patients by first name and last name (custom query)
    List<Patient> findByFirstNameAndLastName(String firstName, String lastName);

    Page<Patient> findByDoctorId(Long doctorId, Pageable pageable);
    long countByDoctorId(Long doctorId);



    boolean existsByDoctorIdAndFirstNameIgnoreCaseAndLastNameIgnoreCase(
            Long doctorId, String firstName, String lastName);

    // Method to fetch the patient details
    Optional<Patient> findByDoctorIdAndFirstNameIgnoreCaseAndLastNameIgnoreCase(
            Long doctorId, String firstName, String lastName);
}
