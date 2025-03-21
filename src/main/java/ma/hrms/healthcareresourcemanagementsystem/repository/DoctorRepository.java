package ma.hrms.healthcareresourcemanagementsystem.repository;


import ma.hrms.healthcareresourcemanagementsystem.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // Custom query to find doctors by specialty name
    @Query("SELECT d FROM Doctor d WHERE d.specialty.name = :specialtyName")
    List<Doctor> findDoctorsBySpecialty(@Param("specialtyName") String specialtyName);

    // Find a doctor by their email
    Optional<Doctor> findByEmail(String email);

    // Find doctors by specialty (example of using a relationship)
    List<Doctor> findBySpecialty_Name(String specialtyName);

    // Find doctors by first name and last name (custom query)
    List<Doctor> findByFirstNameAndLastName(String firstName, String lastName);
}
