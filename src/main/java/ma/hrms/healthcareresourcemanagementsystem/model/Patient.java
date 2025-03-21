package ma.hrms.healthcareresourcemanagementsystem.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patients")
@Setter @Getter
@AllArgsConstructor @NoArgsConstructor @Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "dob")
    private String dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;

    @Override
    public String toString() {
        return "Patient(id=" + id +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", dateOfBirth=" + dateOfBirth +
                ", doctorId=" + (doctor != null ? doctor.getId() : "null") + ")";
    }
}
