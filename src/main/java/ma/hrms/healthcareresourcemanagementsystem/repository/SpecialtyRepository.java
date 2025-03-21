package ma.hrms.healthcareresourcemanagementsystem.repository;

import ma.hrms.healthcareresourcemanagementsystem.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {

    // Find specialty by name
    Optional<Specialty> findByName(String name);
}
