package ma.hrms.healthcareresourcemanagementsystem.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "doctors")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor @Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "years_of_experience")
    private int yearsOfExperience;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "specialty_id", referencedColumnName = "id")
    private Specialty specialty;

    @ToString.Exclude
    @OneToMany(mappedBy = "doctor")
    private List<Patient> patients;

    @Override
    public String toString() {
        return "Doctor(id=" + id +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", email=" + email +
                ", phoneNumber=" + phoneNumber +
                ", gender=" + gender +
                ", yearsOfExperience=" + yearsOfExperience + ")";
    }
}
